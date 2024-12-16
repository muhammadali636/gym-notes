import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;

//GUI imports
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font; 
import javax.swing.JComboBox; 
import javax.swing.JList; 
import javax.swing.JOptionPane; 
import javax.swing.SwingUtilities; 
import javax.swing.ListSelectionModel;

/**
 *GymNotes.java an expansion of DiscussionBoard.java assignment using java swing and SQLITE. 
 *we can add workouts, log workouts, view progress, delete workouts, there is also a welcome page
 */
public class GymNotes extends JFrame implements ActionListener { //swing.
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private JTextArea messagesArea;
    private JPanel leftPanel;

    private Connection conn; //db connection

    public GymNotes() {
        super("GymNotes"); //set the title of the app
        setSize(WIDTH, HEIGHT); //set the app window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the app on exit
        setLayout(new BorderLayout()); //use border layout

        //MENU SETUP
        JMenuBar menuBar = new JMenuBar(); //create menu bar
        JMenu optionsMenu = new JMenu("Options"); //add Options menu

        //create menu items
        JMenuItem welcomeMenu = new JMenuItem("Welcome"); //welcome
        welcomeMenu.setActionCommand("Welcome");
        welcomeMenu.addActionListener(this); //listen for clicks
        optionsMenu.add(welcomeMenu);

        JMenuItem addWorkoutMenu = new JMenuItem("Add Workout"); //add
        addWorkoutMenu.setActionCommand("Add Workout");
        addWorkoutMenu.addActionListener(this);
        optionsMenu.add(addWorkoutMenu);

        JMenuItem deleteWorkoutMenu = new JMenuItem("Delete Workout"); //deleted
        deleteWorkoutMenu.setActionCommand("Delete Workout");
        deleteWorkoutMenu.addActionListener(this);
        optionsMenu.add(deleteWorkoutMenu);

        JMenuItem logWorkoutMenu = new JMenuItem("Log Workout"); //log
        logWorkoutMenu.setActionCommand("Log Workout");
        logWorkoutMenu.addActionListener(this);
        optionsMenu.add(logWorkoutMenu);

        JMenuItem viewProgressMenu = new JMenuItem("View Progress"); //view
        viewProgressMenu.setActionCommand("View Progress");
        viewProgressMenu.addActionListener(this);
        optionsMenu.add(viewProgressMenu);

        //add menu bar to the frame
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);

        //init message area
        messagesArea = new JTextArea(); //create text area for messages
        messagesArea.setEditable(false); //disable editing
        messagesArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); //set font
        JScrollPane scrollPane = new JScrollPane(messagesArea); //add scroll bar
        add(scrollPane, BorderLayout.CENTER); //add it to the center

        //init database SQLITE
        try {
            initDatabase(); //initialize database
            messagesArea.append("Database initialized successfully.\n");
        } 
        catch (Exception e) { //catch any error in initialization
            messagesArea.append("Error initializing database: " + e.getMessage() + "\n");
            e.printStackTrace(); //print error details for debugging
        }

        //welcome panel on startup
        showWelcomePanel(); //show the welcome panel
    }

    /**
     *Init SQLite database and creates necessary tables if they don't exist.
     */
    private void initDatabase() {
        try {
            //load SQLite JDBC driver (sqlite-jdbc-3.47.1.0.jar on path)
            Class.forName("org.sqlite.JDBC"); //load  JDBC driver
            messagesArea.append("SQLite JDBC Driver loaded successfully.\n");
            conn = DriverManager.getConnection("jdbc:sqlite:workoutlogs.db"); //connect to db
            messagesArea.append("Connected to SQLite database.\n");

            //create table if not exist.
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS Workouts (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "workoutName TEXT NOT NULL, " +
                            "description TEXT);");

                stmt.execute("CREATE TABLE IF NOT EXISTS WorkoutLogs (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "workoutId INTEGER NOT NULL, " +
                            "logDate TEXT NOT NULL, " +
                            "weightUsed TEXT, " +
                            "FOREIGN KEY(workoutId) REFERENCES Workouts(id));");
            }
            messagesArea.append("Database tables ensured.\n");
        } 
        catch (ClassNotFoundException e) { //if the driver is missing
            messagesArea.append("SQLite JDBC Driver not found. Please add it to your classpath.\n");
            throw new RuntimeException(e); //terminate app if missing
        } 
        catch (SQLException sqle) { //if a database error occurs
            messagesArea.append("Database error: " + sqle.getMessage() + "\n");
            throw new RuntimeException(sqle);
        }
    }

    /**
     *menu item actions.
     * @param e The ActionEvent triggered by menu items.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand(); //get the action command

        //main options. If any are selected as button. 
        switch (command) {
            case "Welcome": 
                showWelcomePanel();
                break; //break to avoid fall through.
            case "Add Workout": 
                showAddWorkoutPanel();
                break;
            case "Log Workout": 
                showLogWorkoutPanel();
                break;
            case "View Progress":
                showViewProgressPanel();
                break;
            case "Delete Workout": 
                showDeleteWorkoutPanel();
                break;
            default: //unknown option probably should be some kidn of error in this case.
                messagesArea.append("Unknown command: " + command + "\n");
        }
    }

    /**
     *displays the welcome Panel with navigation instructions and a blue background.
     */
    private void showWelcomePanel() {
        clearMessages(); 
        //panel setup:
        leftPanel = new JPanel(); //create a new panel
        leftPanel.setBackground(Color.BLUE); 
        leftPanel.setLayout(new BorderLayout()); 
        JLabel welcomeLabel = new JLabel("Welcome to GymNotes!", JLabel.CENTER); 
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        welcomeLabel.setForeground(Color.WHITE);
        JTextArea instructions = new JTextArea(); 
        instructions.setEditable(false); 
        instructions.setBackground(Color.BLUE); //set background
        instructions.setForeground(Color.WHITE);
        instructions.setFont(new Font("Arial", Font.PLAIN, 16));
        //Welcome panel texts.
        instructions.setText("Navigate through the app using the 'Options' menu at the top:\n\n" +
                            "1. ADD WORKOUT: Add a new workout with a name and description.\n" +
                            "2. DELETE WORKOUT: Remove an existing workout from your list.\n\n" +
                            "3. LOG WORKOUT: Log a workout you performed, including the weight used.\n" +
                            "4. VIEW PROGRESS: View your workout logs for a specific month and year.\n" +
                            "Enjoy the app and get those gains!!!");
        leftPanel.add(welcomeLabel, BorderLayout.NORTH); //add label to the top
        leftPanel.add(instructions, BorderLayout.CENTER); //add instructions to the center
        setLeftPanel(leftPanel); //update the frame
    }

    /**
     *displays Add Workout Panel where the user can add new workouts.
     */
    private void showAddWorkoutPanel() {
        clearMessages(); //clear messages area
        leftPanel = new JPanel(new GridLayout(7, 1, 10, 10)); //use grid layout
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //set border padding
        JLabel addWorkoutLabel = new JLabel("Add Workout", JLabel.CENTER); //create label
        addWorkoutLabel.setFont(new Font("Arial", Font.BOLD, 20)); //set font for label
        JLabel nameLabel = new JLabel("Workout Name:"); //create name label
        JTextField nameField = new JTextField(); //create name field
        JLabel descLabel = new JLabel("Description:"); //create description label
        JTextField descField = new JTextField(); //create description field
        JButton addButton = new JButton("Add"); //create add button
        addButton.setBackground(Color.GREEN); 
        addButton.setForeground(Color.BLACK); 
        addButton.setOpaque(true);
        addButton.setBorderPainted(false); 
        addButton.setFocusPainted(false); 
        addButton.setEnabled(true); //make sure this button is SET TO TRUE

        //add ActionListener without lambda
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wName = nameField.getText().trim(); //get workout name
                String wDesc = descField.getText().trim(); //get workout description
                if (wName.isEmpty()) { //check if name is empty
                    messagesArea.append("Workout name cannot be blank.\n");
                    return;
                }
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Workouts(workoutName, description) VALUES(?,?)")) {
                    ps.setString(1, wName); //set name
                    ps.setString(2, wDesc); //set description
                    ps.executeUpdate(); //execute insert
                    messagesArea.append("Workout added: " + wName + "\n"); //notify user
                    // Clear input fields
                    nameField.setText(""); //clear name field
                    descField.setText(""); //clear description field
                } 
                catch (SQLException sqle) { //catch SQL exception
                    messagesArea.append("Error: " + sqle.getMessage() + "\n"); //display error
                    sqle.printStackTrace(); //print stack trace
                }
            }
        });

        leftPanel.add(addWorkoutLabel); //add label to panel
        leftPanel.add(nameLabel); //add name label
        leftPanel.add(nameField); //add name field
        leftPanel.add(descLabel); //add description label
        leftPanel.add(descField); //add description field
        leftPanel.add(addButton); //add button
        setLeftPanel(leftPanel); //update the frame
    }

    /**
     * Displays the Log Workout Panel where users can log their workouts.
     */
    private void showLogWorkoutPanel() {
        clearMessages(); //clear messages area
        leftPanel = new JPanel(new GridLayout(8, 1, 10, 10)); //use grid layout
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //set border padding
        JLabel logLabel = new JLabel("Log Workout", JLabel.CENTER); //create label
        logLabel.setFont(new Font("Arial", Font.BOLD, 20)); //set font for label
        JLabel workoutLabel = new JLabel("Select Workout:"); //create workout selection label
        JComboBox<String> workoutDropdown = new JComboBox<>(); //create dropdown for workouts
        ArrayList<Integer> workoutIds = new ArrayList<>(); //list to store workout IDs
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, workoutName FROM Workouts")) {
            while (rs.next()) { //iterate through results
                workoutIds.add(rs.getInt("id")); //add workout ID
                workoutDropdown.addItem(rs.getString("workoutName")); //add workout name to dropdown
            }
            if (workoutIds.isEmpty()) { //check if no workouts available
                messagesArea.append("No workouts available. Please add workouts first.\n");
            }
        } 
        //catch problem loading workouts.
        catch (SQLException ex) { //catch SQL exception
            messagesArea.append("Error loading workouts: " + ex.getMessage() + "\n"); //display error
            ex.printStackTrace(); //print stack trace
        }

        JLabel weightLabel = new JLabel("Weight (optional):"); //create weight label
        JTextField weightField = new JTextField(); //create weight field
        JButton logButton = new JButton("Log"); //create log button
        logButton.setBackground(Color.BLUE); 
        logButton.setForeground(Color.WHITE);
        logButton.setOpaque(true); 
        logButton.setBorderPainted(false); //remove border
        logButton.setFocusPainted(false); 
        logButton.setEnabled(true); //MAKE SURE button enabled

        //add ActionListener 
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIdx = workoutDropdown.getSelectedIndex(); //get selected index
                if (selectedIdx < 0) { //check if no selection
                    messagesArea.append("No workout selected.\n");
                    return;
                }
                int workoutId = workoutIds.get(selectedIdx); //get workout ID
                String weightUsed = weightField.getText().trim(); //get weight used
                if (weightUsed.isEmpty()) { //check if weight is empty
                    weightUsed = "N/A"; //set to N/A if empty
                }

                LocalDate today = LocalDate.now(); //get current date
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO WorkoutLogs(workoutId, logDate, weightUsed) VALUES(?,?,?)")) {
                    ps.setInt(1, workoutId); //set workout ID
                    ps.setString(2, today.toString()); //set log date
                    ps.setString(3, weightUsed); //set weight used
                    ps.executeUpdate(); //execute insert
                    messagesArea.append("Logged workout for " + workoutDropdown.getSelectedItem() + " on " + today + " with weight: " + weightUsed + "\n"); //notify user
                    //clear input fields
                    weightField.setText(""); //clear weight field
                } 
                catch (SQLException ex) { //catch SQL exception
                    messagesArea.append("Error: " + ex.getMessage() + "\n"); //display error
                    ex.printStackTrace(); //print stack trace
                }
            }
        });
        leftPanel.add(logLabel); //add label to panel
        leftPanel.add(workoutLabel); //add workout label
        leftPanel.add(workoutDropdown); //add workout dropdown
        leftPanel.add(weightLabel); //add weight label
        leftPanel.add(weightField); //add weight field
        leftPanel.add(logButton); //add log button

        setLeftPanel(leftPanel); //update the frame
    }

    /**
     *displays the View Progress screen where users can view their workout logs.
     * If month and/or year are not entered then set that parameter as open and send back all retrievals.
     */
    private void showViewProgressPanel() {
        clearMessages(); 
        leftPanel = new JPanel(new GridLayout(8, 1, 10, 10)); //use grid layout
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //set border padding
        JLabel viewLabel = new JLabel("View Progress", JLabel.CENTER); 
        viewLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
        JLabel monthLabel = new JLabel("Month (1-12):"); 
        JTextField monthField = new JTextField(); 
        JLabel yearLabel = new JLabel("Year (YYYY):"); 
        JTextField yearField = new JTextField();
        JButton viewButton = new JButton("View"); 
        viewButton.setBackground(Color.BLUE); 
        viewButton.setForeground(Color.WHITE); 
        viewButton.setOpaque(true); 
        viewButton.setBorderPainted(false); 
        viewButton.setFocusPainted(false); 
        viewButton.setEnabled(true); //ensure button enables

        //action listener 
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String monthStr = monthField.getText().trim(); //get month input
                String yearStr = yearField.getText().trim(); //get year input

                //init query comps.
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("SELECT wl.logDate, w.workoutName, wl.weightUsed ")
                            .append("FROM WorkoutLogs wl ")
                            .append("JOIN Workouts w ON wl.workoutId = w.id ");

                ArrayList<String> conditions = new ArrayList<>(); //list for conditions
                ArrayList<String> parameters = new ArrayList<>(); //list for parameters

                if (!monthStr.isEmpty()) { //check if month is entered
                    //validate month
                    try {
                        int month = Integer.parseInt(monthStr); //parse month
                        if (month < 1 || month > 12) { //validate month range
                            messagesArea.append("Invalid month. Please enter a value between 1 and 12.\n");
                            return;
                        }
                        //month should be 2 digits
                        String formattedMonth = String.format("%02d", month); //format month
                        conditions.add("wl.logDate LIKE ?");
                        parameters.add("%-" + formattedMonth + "-%");
                    } 
                    catch (NumberFormatException nfe) { //catch number format exception
                        messagesArea.append("Invalid month format. Please enter a numeric value.\n");
                        return;
                    }
                }

                if (!yearStr.isEmpty()) { //check if year is entered
                    //validate year
                    try {
                        int year = Integer.parseInt(yearStr); //parse year
                        if (year < 0) { //validate year is positive
                            messagesArea.append("Invalid year. Please enter a positive value.\n");
                            return;
                        }
                        conditions.add("wl.logDate LIKE ?");
                        parameters.add(yearStr + "-%");
                    } 
                    catch (NumberFormatException nfe) { //catch number format exception
                        messagesArea.append("Invalid year format. Please enter a numeric value.\n");
                        return;
                    }
                }

                //PUT WHERE if there are any conditions
                if (!conditions.isEmpty()) {
                    queryBuilder.append("WHERE ");
                    for (int i = 0; i < conditions.size(); i++) {
                        queryBuilder.append(conditions.get(i));
                        if (i < conditions.size() - 1) {
                            queryBuilder.append(" AND ");
                        }
                    }
                }

                queryBuilder.append(" ORDER BY wl.logDate ASC"); //order results
                String finalQuery = queryBuilder.toString(); //final query string

                try (PreparedStatement ps = conn.prepareStatement(finalQuery)) { //prepare statement
                    //set param.
                    for (int i = 0; i < parameters.size(); i++) {
                        ps.setString(i + 1, parameters.get(i)); //set each parameter
                    }
                    try (ResultSet rs = ps.executeQuery()) { //execute query
                        boolean found = false; //flag to check if any records found
                        if (conditions.isEmpty()) {
                            messagesArea.append("Displaying all workouts:\n");
                        } 
                        else if (!monthStr.isEmpty() && !yearStr.isEmpty()) {
                            messagesArea.append("Progress for " + monthStr + "/" + yearStr + ":\n");
                        } 
                        else if (!monthStr.isEmpty()) {
                            messagesArea.append("Progress for Month: " + monthStr + " (All Years):\n");
                        } 
                        else { //if only year str empty
                            messagesArea.append("Progress for Year: " + yearStr + " (All Months):\n");
                        }
                        while (rs.next()) { //iterate through results
                            found = true;
                            messagesArea.append(rs.getString("logDate") + ": " 
                                + rs.getString("workoutName") + " - Weight: " 
                                + rs.getString("weightUsed") + "\n");
                        }
                        if (!found) { //if no records found
                            messagesArea.append("No workouts found for the specified criteria.\n");
                        }
                    }
                } 
                catch (SQLException ex) { //catch SQL exception
                    messagesArea.append("Error: " + ex.getMessage() + "\n"); //display error
                    ex.printStackTrace(); //print stack trace
                }
            }
        });
        
        //add labels
        leftPanel.add(viewLabel); 
        leftPanel.add(monthLabel); 
        leftPanel.add(monthField); 
        leftPanel.add(yearLabel); 
        leftPanel.add(yearField); 
        leftPanel.add(viewButton);
        setLeftPanel(leftPanel); //update the frame
    }

    /**
     *display Delete Workout Panel where users can delete existing workouts..
     */
    private void showDeleteWorkoutPanel() {
        clearMessages(); //clear messages area
        leftPanel = new JPanel(new BorderLayout(10, 10)); //use border layout with gaps
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //set border padding

        JLabel deleteLabel = new JLabel("Delete Workout", JLabel.CENTER); //create label
        deleteLabel.setFont(new Font("Arial", Font.BOLD, 20)); //set font for label

        // Fetch workouts from the database
        ArrayList<Workout> workouts = getAllWorkouts(); //get all workouts

        if (workouts.isEmpty()) { //check if no workouts available
            messagesArea.append("No workouts available to delete. Please go to addworkout menu\n");
            return;
        }

        String[] workoutNames = new String[workouts.size()]; //array to store workout names.
        //set workoutnames
        for (int i = 0; i < workouts.size(); i++) { 
            workoutNames[i] = workouts.get(i).getWorkoutName();
        }

        JList<String> workoutList = new JList<>(workoutNames); //create list for workouts
        workoutList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //single selection mode
        workoutList.setVisibleRowCount(10); //set visible rows
        JScrollPane listScrollPane = new JScrollPane(workoutList); //add scroll pane

        JButton deleteButton = new JButton("Delete Selected Workout"); //create delete button
        deleteButton.setBackground(Color.RED); 
        deleteButton.setForeground(Color.WHITE); 
        deleteButton.setOpaque(true); 
        deleteButton.setBorderPainted(false); 
        deleteButton.setFocusPainted(false); 
        deleteButton.setEnabled(true); //set true must

        //add ActionListener without lambda
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIdx = workoutList.getSelectedIndex(); //get selected index
                if (selectedIdx < 0) { //check if no selection.
                    messagesArea.append("No workout selected to delete.\n");
                    return;
                }
                Workout selectedWorkout = workouts.get(selectedIdx); //get selected workout.
                int confirm = JOptionPane.showConfirmDialog(
                        GymNotes.this,
                        "Are you sure you want to delete the workout: " + selectedWorkout.getWorkoutName() + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                ); //ask for confirmation
                if (confirm == JOptionPane.YES_OPTION) { //if user confirms
                    try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Workouts WHERE id = ?")) {
                        ps.setInt(1, selectedWorkout.getId()); //set workout ID
                        ps.executeUpdate(); //execute delete
                        messagesArea.append("Deleted workout: " + selectedWorkout.getWorkoutName() + "\n"); //notify user
                        showDeleteWorkoutPanel(); // Refresh the panel
                    } 
                    catch (SQLException ex) { //catch SQL exception
                        messagesArea.append("Error deleting workout: " + ex.getMessage() + "\n"); //display error
                        ex.printStackTrace(); //print stack trace
                    }
                }
            }
        });

        leftPanel.add(deleteLabel, BorderLayout.NORTH); //add label to top
        leftPanel.add(listScrollPane, BorderLayout.CENTER); //add list to center
        leftPanel.add(deleteButton, BorderLayout.SOUTH); //add button to bottom

        setLeftPanel(leftPanel); //update the frame
    }

    /**
     * Retrieves all workouts from the database.
     * @return An ArrayList of Workout objects.
     */
    private ArrayList<Workout> getAllWorkouts() {
        ArrayList<Workout> workouts = new ArrayList<>(); //create list to store workouts
        String query = "SELECT id, workoutName, description FROM Workouts"; //SQL query to fetch workouts
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) { //execute query
            while (rs.next()) { //iterate through results
                Workout workout = new Workout(
                    rs.getInt("id"), //get workout ID
                    rs.getString("workoutName"), //get workout name
                    rs.getString("description") //get workout description
                );
                workouts.add(workout); //add to list
            }
        } 
        catch (SQLException ex) { //catch SQL exception
            messagesArea.append("Error fetching workouts: " + ex.getMessage() + "\n"); //display error
            ex.printStackTrace(); //print stack trace
        }
        return workouts; //return list of workouts
    }

    /**
     * Sets the left panel of the JFrame.
     *
     * @param panel The JPanel to set as the left panel.
     */
    private void setLeftPanel(JPanel panel) {
        getContentPane().removeAll(); //clear the current frame contents.
        add(panel, BorderLayout.WEST); //add the new panel to the left.
        add(new JScrollPane(messagesArea), BorderLayout.CENTER); //keep messages area
        revalidate(); //refresh the frame
        repaint(); //repaint frame
    }

    /**
     * Clears the messages area.
     */
    private void clearMessages() {
        messagesArea.setText(""); //clear the messages area
    }

    /**
     *main method to launch app
     *@param args Commandline args
     */
    public static void main(String[] args) {
        //global exception handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace(); //print stack trace for any uncaught exceptions
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GymNotes gui = new GymNotes(); //create instance of GymNotes
                gui.setVisible(true); //GUI IS ON
            }
        });
    }
}

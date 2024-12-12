
/*
 DiscussionBoard5,0 for LAB NOW WITH GUI. Ignore polls
 Muhammad Ali, 1115336
 Compile: javac DiscussionBoard.java
Run: java DiscussionBoard
 */

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;


//User class representing a user in  discussion board
class User {
    private String fullName;       //Users full nam
    private String username; //User username. 

    public User(String fullName, String username) throws Exception {
        if (fullName == null || fullName.isBlank()) {      //Throw exception when  full name is blank.
            throw new Exception("Full name cannot be blank");
        }
        if (username == null || username.isBlank()) {     //Throw exception if user is blank liek above
            throw new Exception("Username cannot be blank");
        }
        this.fullName = fullName;
        this.username = username;
    }

    public String getFullName() { //ret user full name
        return fullName;
    }

    public String getUsername() { //ret  user username
        return username;
    }
}

//Post class represents posts in  discussion board
class Post {
    private static int idCounter = 1;      //unique ID counter for posts
    private int postId;
    private String title;
    private User user;

    public Post(String title, User user) throws Exception {
        if (title == null || title.isBlank()) { //throw exception if title is blank
            throw new Exception("Title cannot be blank");
        }

        this.postId = idCounter; //assign a unique ID
        idCounter++;
        this.title = title;
        this.user = user;
    }

    public void display() { //display  post details
        System.out.println("Post #" + postId);
        System.out.println("Created By: " + user.getFullName() + " (@" + user.getUsername() + ")");
        System.out.println("Title: " + title);
    }

    //GETTERS
    public int getPostId() { //return post ID
        return postId;
    }

    public String getTitle() {      //return the post title
        return title;
    }

    public User getUser() {       //return the user who created the post
        return user;
    }
}

//textPost class represents a text post
class textPost extends Post {
    private String content;
    public textPost(String title, String content, User user) throws Exception {
        super(title, user);   //init with the superclass constructor
        if (content == null || content.isBlank()) {   //throw exception if content is blank
            throw new Exception("Content cannot be blank");
        }

        this.content = content;
    }
    public void display() { //display  post content
        super.display();
        System.out.println(content);
    }
    public String getContent() {        //return post content
        return content;
    }
}

 //  pollPost class represents a poll post with voting options. This is  Not gonna be used rn. Maybe in future.
 class pollPost extends Post {
    private ArrayList<String> options;    //list of options for voting
    private ArrayList<Integer> votes;   //vote count for each option

    public pollPost(String title, String optionsString, User user) throws Exception {
        super(title, user);
        options = new ArrayList<>();
        votes = new ArrayList<>();
        for (String option : optionsString.split(";")) {
            options.add(option.trim());
            votes.add(0);
        }
    }
    //?????
    public void vote(int optionIndex) {
        if (optionIndex >= 0 && optionIndex < votes.size()) {
            votes.set(optionIndex, votes.get(optionIndex) + 1);
        } 
        else {
            System.out.println("Invalid option selected.");
        }
    }

    public void display() {
        super.display();
        for (int i = 0; i < options.size(); i++) {
            System.out.println(options.get(i) + ": " + votes.get(i));
        }
    }

    //getters
    public ArrayList<String> getOptions() {
        return options;
    }

    public ArrayList<Integer> getVotes() {
        return votes;
    }
}

//DiscussionBoard class with GUI 
public class DiscussionBoard extends JFrame implements ActionListener {
    public static final int WIDTH = 600; // window width
    public static final int HEIGHT = 400; //window height

    private static ArrayList<User> users = new ArrayList<>(); //list of regist users
    private static ArrayList<Post> posts = new ArrayList<>(); //list of posts
    private static HashMap<String, ArrayList<Integer>> userPostIndex = new HashMap<>(); //map usernames to post indices

    private JTextArea messagesArea; //area fiedldto display messages
    private JPanel leftPanel; //panel for user inputs

    public DiscussionBoard() {
        super("Discussion Board");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar(); //menu bar for navigation
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem registerMenu = new JMenuItem("Register User");
        registerMenu.addActionListener(this); //action listener for  register menu
        optionsMenu.add(registerMenu);

        JMenuItem createPostMenu = new JMenuItem("Create Text Post");
        createPostMenu.addActionListener(this); //action listener for  create post menu
        optionsMenu.add(createPostMenu);

        JMenuItem searchMenu = new JMenuItem("Search Posts");
        searchMenu.addActionListener(this); //action listener for  search menu
        optionsMenu.add(searchMenu);

        menuBar.add(optionsMenu); //add menu to  menu bar
        setJMenuBar(menuBar);

        messagesArea = new JTextArea(); //text area to display messages
        messagesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messagesArea);
        add(scrollPane, BorderLayout.CENTER);

        showRegisterPanel(); //display  register user panel by default
    }

    public void actionPerformed(ActionEvent e) { //handle menu actions
        String command = e.getActionCommand();

        if (command.equals("Register User")) {
            showRegisterPanel();
        } 
        else if (command.equals("Create Text Post")) {
            showCreatePostPanel();
        } 
        else if (command.equals("Search Posts")) {
            showSearchPanel();
        }
    }

    private void showRegisterPanel() { //display  register user panel
        clearMessages();
        leftPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel registerLabel = new JLabel("Register User", JLabel.CENTER);
        JLabel fullNameLabel = new JLabel("Full Name:");
        JTextField fullNameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.BLUE); //set blue background (this is following lab guide slides)
        registerButton.setForeground(Color.WHITE); //set white text
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fullName = fullNameField.getText().trim();
                String username = usernameField.getText().trim();

                try {
                    for (User user : users) {
                        if (user.getUsername().equalsIgnoreCase(username)) {
                            throw new Exception("Username already exists.");
                        }
                    }

                    User newUser = new User(fullName, username);
                    users.add(newUser);
                    messagesArea.append("User registered successfully: " + username + "\n");
                } 
                catch (Exception ex) {
                    messagesArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });

        leftPanel.add(registerLabel);
        leftPanel.add(fullNameLabel);
        leftPanel.add(fullNameField);
        leftPanel.add(usernameLabel);
        leftPanel.add(usernameField);
        leftPanel.add(registerButton);

        setLeftPanel(leftPanel);
    }

    private void showCreatePostPanel() { //display create post panel
        clearMessages();
        leftPanel = new JPanel(new GridLayout(6, 1, 5, 5));

        JLabel createPostLabel = new JLabel("Create Text Post", JLabel.CENTER);
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel postBodyLabel = new JLabel("Post Body:");
        JTextField postBodyField = new JTextField();

        JButton createButton = new JButton("Create");
        createButton.setBackground(Color.BLUE); //set blue background (following lab guide)
        createButton.setForeground(Color.WHITE); //set white text
        createButton.setOpaque(true);
        createButton.setBorderPainted(false);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String postBody = postBodyField.getText().trim();

                try {
                    User userFound = null;

                    for (User user : users) {
                        if (user.getUsername().equalsIgnoreCase(username)) {
                            userFound = user;
                            break;
                        }
                    }

                    if (userFound == null) {
                        throw new Exception("User not found.");
                    }

                    if (postBody.isEmpty()) {
                        throw new Exception("Post content cannot be blank.");
                    }

                    textPost newPost = new textPost("Text Post", postBody, userFound);
                    posts.add(newPost);
                    addToUserPostIndex(username, posts.size() - 1);
                    messagesArea.append("Post created successfully by: " + username + "\n");
                } 
                catch (Exception ex) {
                    messagesArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });
        leftPanel.add(createPostLabel);
        leftPanel.add(usernameLabel);
        leftPanel.add(usernameField);
        leftPanel.add(postBodyLabel);
        leftPanel.add(postBodyField);
        leftPanel.add(createButton);
        setLeftPanel(leftPanel);
    }

    private void showSearchPanel() { //display search posts panel
        clearMessages();
        leftPanel = new JPanel(new GridLayout(6, 1, 5, 5));

        JLabel searchLabel = new JLabel("Search Posts", JLabel.CENTER);
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(Color.BLUE); //set blue background for search (we used thi sbefore).
        searchButton.setForeground(Color.WHITE); //set white text
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        //UGH
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim().toLowerCase();

                try {
                    if (username.isEmpty()) {
                        throw new Exception("Username cannot be blank.");
                    }

                    ArrayList<Integer> postIndices = userPostIndex.get(username);

                    if (postIndices != null && !postIndices.isEmpty()) {
                        messagesArea.append("Posts by @" + username + ":\n"); //following sample guide

                        for (int index : postIndices) {
                            Post post = posts.get(index);

                            if (post instanceof textPost) {
                                textPost text = (textPost) post;
                                messagesArea.append("Post #" + text.getPostId() + "\n");
                                messagesArea.append("Created By: " + text.getUser().getFullName() + " (@" + text.getUser().getUsername() + ")\n");
                                messagesArea.append("Title: " + text.getTitle() + "\n");
                                messagesArea.append(text.getContent() + "\n\n");
                            }
                        }
                    } 
                    else {
                        messagesArea.append("No posts found for user: @" + username + "\n");
                    }
                } 
                catch (Exception ex) { //caught
                    messagesArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });

        leftPanel.add(searchLabel);
        leftPanel.add(usernameLabel);
        leftPanel.add(usernameField);
        leftPanel.add(searchButton);

        setLeftPanel(leftPanel);
    }
    private void addToUserPostIndex(String username, int postIndex) { //add post index to user list
        userPostIndex.putIfAbsent(username.toLowerCase(), new ArrayList<>());
        userPostIndex.get(username.toLowerCase()).add(postIndex);
    }
    private void setLeftPanel(JPanel panel) { //update left panel with tnew panel
        getContentPane().removeAll();
        add(panel, BorderLayout.WEST);
        add(new JScrollPane(messagesArea), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    private void clearMessages() { //clear msgs
        messagesArea.setText("");
    }
    public static void main(String[] args) { //main method 
        DiscussionBoard gui = new DiscussionBoard();
        gui.setVisible(true); //make  GUI visible from lecture
    }
}
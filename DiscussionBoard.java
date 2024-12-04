/**
*DiscussionBoard2.0 for Lab2
*Muhammad Ali, 1115336
*Compile: javac DiscussionBoard.java
*Run: java DiscussionBoard 
**/
import java.util.ArrayList; 
import java.util.Scanner;
import java.util.StringTokenizer;

//user class represents user in discussionboard. 
class User 
{
    //each user has a full name and username,
    public String fullName;
    public String username;

    public User(String fullName, String username) {
        this.fullName = fullName; //set full name
        //if no username provided NULL or only spaces we have to default to firstname. Use the method onlySpaceChecker to help.
        if (username == null || onlySpaceChecker(username)) {
            this.username = fullName.split(" ")[0].toLowerCase(); 
        } 
        //else use username
        else {
            this.username = username.toLowerCase(); 
        }
    }

    //check if the string contains only spaces
    private boolean onlySpaceChecker(String user) {
        for (int i = 0; i < user.length(); i++) {
            if (user.charAt(i) != ' ') {
                return false; //means it found nonspace character
            }
        }
        return true; //all characters are spaces.
    }
}
//post class represents posts in discussionboard.  
class Post 
{
    //each post has title, content, and user. The user's first name is stored.
    public String title;
    public String content;
    public User user;

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}

//main class
public class DiscussionBoard 
{
    //main method
    public static void main(String[] args) 
    {
        ArrayList<User> users = new ArrayList<>();  //list of users
        ArrayList<Post> posts = new ArrayList<>();  //list of posts
        Scanner scanner = new Scanner(System.in);   
        while (true) 
        {
            //command loop with options
            System.out.println("Select one:");
            System.out.println("(1) Create new user");
            System.out.println("(2) Create new post");
            System.out.println("(3) View all posts");
            System.out.println("(4) View all posts with a given username");
            System.out.println("(5) View all posts with a given keyword");
            System.out.println("(6) End Program");

            int operator = scanner.nextInt();  //choice for user selection
            scanner.nextLine();  //eat newline char \n

            switch (operator) 
            {
                //create new user
                case 1:
                    System.out.println("Enter your name in full please: ");
                    String fullName = scanner.nextLine(); //User full name input
                    System.out.println("Enter your username (this is optional): ");
                    String username = scanner.nextLine(); //username input OPTIONAL

                    //check if user exist already
                    boolean userExists = false;
                    for (User user : users) {
                        if (user.username.equals(username.toLowerCase())) { //compare usernames in lowercase
                            userExists = true;
                            break;
                        }
                    }
                    //if user doesn't exist register new user
                    if (!userExists) { 
                        User newUser = new User(fullName, username); //make new user
                        users.add(newUser); //add user to list
                        System.out.println("User created successfully!");
                    }
                    //handling of duplicate user 
                    else {
                        System.out.println("Username already exists. Try again!"); 
                    }
                    break;

                //make new post
                case 2:
                    System.out.println("Enter your username to post: ");
                    String postUsername = scanner.nextLine().toLowerCase(); //get username to search for
                    User userFound = null;
                    //search for user in the user list.
                    for (User user : users) 
                    {
                        if (user.username.equals(postUsername)) {
                            userFound = user;
                            break; //if user found leave.
                        }
                    }

                    //if user is found create a new post.
                    if (userFound != null) 
                    {
                        System.out.println("Enter your title for your post: ");
                        String title = scanner.nextLine(); //input for post title
                        System.out.println("Now Enter the content for your post: ");
                        String content = scanner.nextLine(); //input for post content
                        posts.add(new Post(title, content, userFound)); //get post rthat is linked with user
                        System.out.println("Post added successfully!");
                    }
                    //case when user not found 
                    else {
                        System.out.println("User not found. You must register first!!!");
                    }
                    break;

                //see all posts
                case 3:
                    //case when there are no posts
                    if (posts.isEmpty()) {
                        System.out.println("Nothing has been posted yet!");
                    } 
                    else 
                    {
                        //search in post list and pritn out every post.
                        for (Post post : posts) {
                            System.out.println("Created By: " + post.user.fullName + " (@" + post.user.username + ")");
                            System.out.println("Title: " + post.title);
                            System.out.println(post.content);
                        }
                    }
                    break;

                //view all posts by name (case sensitive)
                case 4:
                    System.out.println("Enter a username to search for: ");
                    String searchUser = scanner.nextLine().toLowerCase(); //get username to search for
                    boolean postFound = false;

                    //search in post list, if found then set postFound to true.
                    for (Post post : posts) 
                    {
                        if (post.user.username.equals(searchUser)) {
                            System.out.println("Created By: " + post.user.fullName + " (@" + post.user.username + ")");
                            System.out.println("Title: " + post.title);
                            System.out.println(post.content);
                            postFound = true;
                        }
                    }
                    //case when no post found print nothing found.
                    if (!postFound) {
                        System.out.println("No posts found for this username.");
                    }
                    break;

                //View all posts with a given keyword (case insensitive) TA ON DISCUSSION SAID ONLY CHECK POST NOT TITLE.
                case 5:
                    System.out.println("Enter a keyword to search: ");
                    String keyword = scanner.nextLine().toLowerCase(); //get keyword to search for

                    postFound = false; 

                    //iterate through the post list and search for the keyword 
                    for (Post post : posts) 
                    {
                        StringTokenizer tokenizer = new StringTokenizer(post.content.toLowerCase()); //tekonize the content by spaces
                        //go through the tokens aka the words in the posts via while loop
                        while (tokenizer.hasMoreTokens()) {
                            String word = tokenizer.nextToken(); 
                            //check for if the word matches the keyword
                            if (word.equals(keyword)) { 
                                postFound = true; //post found!!!!!!!!!!!
                                System.out.println("Created By: " + post.user.fullName + " (@" + post.user.username + ")");
                                System.out.println("Title: " + post.title);
                                System.out.println(post.content);
                                break; //exit loop
                            }
                        }
                    }
                    //if nothing found :(
                    if (!postFound) {
                        System.out.println("No posts found containing this keyword."); //handle case when no posts match the keyword
                    }
                    break;

                //End program.
                case 6:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid input. Try again."); //wrong operater
            }
        }
    }
}
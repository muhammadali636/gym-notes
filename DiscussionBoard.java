/**
 * DiscussionBoard4.0 for Lab4 NOW WITH HASHMAP
 * Muhammad Ali, 1115336
 * Compile: javac DiscussionBoard.java
 * Run: java DiscussionBoard <filename>
 */

 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.PrintWriter;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Scanner;
 import java.util.StringTokenizer;
 
 //User class representing a user in the discussion board
 class User {
     private String fullName;
     private String username;
 
     public User(String fullName, String username) throws Exception {
         if (fullName == null || fullName.isBlank()) { //throw exception when the full name is blank
             throw new Exception("Full name cannot be blank");
         }
         if (username == null || username.isBlank()) { //throw exception if username is blank
             throw new Exception("Username cannot be blank");
         }
         this.fullName = fullName;
         this.username = username;
     }
 
     public String getFullName() { //the name
         return fullName;
     }
 
     public String getUsername() { //user
         return username;
     }
 }
 
 //Post class represents posts in discussion board
 class Post {
     //Each post has title, content, and user. The user's first name is stored.
     private static int idCounter = 1; //UNIQUE ID counter for posts
     private int postId;
     private String title;
     private User user;
 
     public Post(String title, User user) throws Exception {
         if (title == null || title.isBlank()) { //throw exception if title is blank.
             throw new Exception("Title cannot be blank");
         }
         this.postId = idCounter; //assign unique ID
         idCounter += 1; //UPP the ID by 1 each time
         this.title = title;
         this.user = user;
     }
 
     public void display() {
         System.out.println("Post #" + postId);
         System.out.println("Created By: " + user.getFullName() + " (@" + user.getUsername() + ")");
         System.out.println("Title: " + title);
     }
 
     //getters
     public int getPostId() {
         return postId;
     }
 
     public String getTitle() {
         return title;
     }
 
     public User getUser() {
         return user;
     }
 }
 
 //textPost class represents a regular text post in discussion board
 class textPost extends Post {
     private String content;
 
     public textPost(String title, String content, User user) throws Exception {
         super(title, user); //superclass constructor to set title and user
         if (content == null || content.isBlank()) { //throw exception if content is blank
             throw new Exception("Content cannot be blank");
         }
         this.content = content;
     }
 
     public void display() {
         super.display();
         System.out.println(content); //print content only once
     }
 
     public String getContent() {
         return content;
     }
 }
 
 //pollPost class represents a poll post with voting options
 class pollPost extends Post {
     private ArrayList<String> options; //list of options for voting
     private ArrayList<Integer> votes; //vote count for each option
 
     public pollPost(String title, String optionsString, User user) throws Exception {
         super(title, user);
         options = new ArrayList<>();
         votes = new ArrayList<>();
         for (String option : optionsString.split(";")) {
             options.add(option.trim());
             votes.add(0);
         }
     }
 
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
 
 public class DiscussionBoard {
     private static ArrayList<User> users = new ArrayList<>(); //list of users
     private static ArrayList<Post> posts = new ArrayList<>(); //list of posts
     private static HashMap<String, ArrayList<Integer>> userPostIndex = new HashMap<>(); //HashMap to store post indices by username
 
     private static void addToUserPostIndex(String username, int postIndex) { //add post index to the user's list in the HashMap
         userPostIndex.putIfAbsent(username, new ArrayList<>()); //initialize list for new username
         userPostIndex.get(username).add(postIndex); //add post index to user's list
     }
 
     private static int getValidInt(Scanner scanner, String prompt) { //prompt user until valid integer input is received
         while (true) {
             try {
                 System.out.println(prompt);
                 return Integer.parseInt(scanner.nextLine().trim());
             } 
             catch (NumberFormatException e) {
                 System.out.println("Invalid input. Please enter a valid integer."); //if not valid input.
             }
         }
     }
 
     public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);
 
         //Load from file if filename provided
         if (args.length > 0) {
             String filename = args[0];
             try (Scanner fileScanner = new Scanner(new FileInputStream("boards/" + filename + ".dboard"))) {
                 User currentUser = null;
                 String title = null;
                 String content = null;
                 ArrayList<String> options = new ArrayList<>();
                 ArrayList<Integer> votes = new ArrayList<>();
 
                 while (fileScanner.hasNextLine()) {
                     String line = fileScanner.nextLine().trim();
 
                     if (line.startsWith("Post #")) {
                         options.clear();
                         votes.clear();
                         content = null;
                     } 
                     else if (line.startsWith("Created By:")) {
                         String[] userParts = line.split(" @");
                         String fullName = userParts[0].replace("Created By: ", "").trim();
                         String username = userParts[1].replace(")", "").trim();
                         currentUser = new User(fullName, username);
                     } 
                     else if (line.startsWith("Title:")) {
                         title = line.replace("Title: ", "").trim();
                     } 
                     else if (line.contains(":")) {
                         String[] optionParts = line.split(":");
                         options.add(optionParts[0].trim());
                         votes.add(Integer.parseInt(optionParts[1].trim()));
                     } 
                     else if (!line.isEmpty()) {
                         content = line;
                     } 
                     else if (line.isEmpty()) {
                         if (content != null) {
                             posts.add(new textPost(title, content, currentUser));
                         } 
                         else if (!options.isEmpty()) {
                             pollPost poll = new pollPost(title, String.join(";", options), currentUser);
                             for (int i = 0; i < votes.size(); i++) {
                                 poll.getVotes().set(i, votes.get(i));
                             }
                             posts.add(poll);
                         }
                     }
                 }
                 System.out.println("Discussion board loaded from file.");
             } 
             catch (FileNotFoundException e) {
                 System.out.println("File not found. Starting with a blank discussion board.");
             } 
             catch (Exception e) {
                 System.out.println("Error loading file: " + e.getMessage());
             }
         }
 
         //command loop with options
         while (true) {
             System.out.println("Select one:");
             System.out.println("(1) Create new user");
             System.out.println("(2) Create new post");
             System.out.println("(3) View all posts");
             System.out.println("(4) Vote in poll");
             System.out.println("(5) View all posts with a given username");
             System.out.println("(6) View all posts with a given keyword");
             System.out.println("(7) Save Discussion Board");
             System.out.println("(8) End Program");
 
             int operator = getValidInt(scanner, "Enter your choice:");
 
             try {
                 switch (operator) {
                     case 1: //create new user
                         System.out.println("Enter your full name:");
                         String fullName = scanner.nextLine();
                         System.out.println("Enter a username:");
                         String username = scanner.nextLine();
 
                         boolean userExists = false;
                         for (User user : users) {
                             if (user.getUsername().equals(username)) {
                                 userExists = true;
                                 break;
                             }
                         }
                         if (!userExists) {
                             User newUser = new User(fullName, username);
                             users.add(newUser);
                             System.out.println("User created.");
                         } 
                         else {
                             System.out.println("Username exists.");
                         }
                         break;
 
                     case 2: //create new post
                         System.out.println("Enter post type (text or poll):");
                         String postType = scanner.nextLine().toLowerCase();
                         System.out.println("Enter your username:");
                         String postUsername = scanner.nextLine();
                         User userFound = null;
 
                         for (User user : users) {
                             if (user.getUsername().equals(postUsername)) {
                                 userFound = user;
                                 break;
                             }
                         }
 
                         if (userFound != null) {
                             System.out.println("Enter the title of your post:");
                             String title = scanner.nextLine();
 
                             if (postType.equals("text")) {
                                 System.out.println("Enter content:");
                                 String content = scanner.nextLine();
                                 posts.add(new textPost(title, content, userFound));
                                 addToUserPostIndex(userFound.getUsername(), posts.size() - 1);
                                 System.out.println("Text post added.");
 
                             } 
                             else if (postType.equals("poll")) {
                                 System.out.println("Enter poll options separated by ';':");
                                 String optionsString = scanner.nextLine();
                                 pollPost newPoll = new pollPost(title, optionsString, userFound);
                                 posts.add(newPoll);
                                 addToUserPostIndex(userFound.getUsername(), posts.size() - 1);
                                 System.out.println("Poll post added.");
                             } 
                             else {
                                 System.out.println("Invalid post type.");
                             }
                         } 
                         else {
                             System.out.println("User not found. Register first!");
                         }
                         break;
 
                     case 3: //view all posts
                         if (posts.isEmpty()) {
                             System.out.println("Nothing has been posted yet!");
                         } 
                         else {
                             for (Post post : posts) {
                                 post.display(); //call display on each post
                                 System.out.println(); //line for separation
                             }
                         }
                         break;
 
                     case 4: //vote in poll
                         System.out.println("Enter poll post ID to vote in:");
                         int pollId = scanner.nextInt();
                         scanner.nextLine(); //consume newline
 
                         Post pollPost = null;
 
                         //find pollpost VIA ID
                         for (Post post : posts) {
                             if (post.getPostId() == pollId && post instanceof pollPost) {
                                 pollPost = post;
                                 break;
                             }
                         }
 
                         //check if poll post was found and check if its a poll
                         if (pollPost instanceof pollPost) {
                             pollPost poll = (pollPost) pollPost;
 
                             //show poll options
                             System.out.println("Poll: " + poll.getTitle());
                             for (int i = 0; i < poll.getOptions().size(); i++) {
                                 System.out.println((i + 1) + ". " + poll.getOptions().get(i) + ": " + poll.getVotes().get(i));
                             }
 
                             //prompt for the user choice
                             System.out.println("Select an option number to vote:");
                             int choice = scanner.nextInt() - 1;
                             scanner.nextLine(); //consume newline
 
                             //check choice and update the vote count
                             if (choice >= 0 && choice < poll.getOptions().size()) {
                                 poll.vote(choice);
                                 System.out.println("Vote recorded successfully in poll ID: " + poll.getPostId());
                             } 
                             else {
                                 System.out.println("Invalid choice.");
                             }
                         } else {
                             System.out.println("Invalid poll ID!");
                         }
                         break;
 
                     case 5: //view all posts from given username
                         System.out.println("Enter a username to search for:");
                         String searchUser = scanner.nextLine().toLowerCase();
                         ArrayList<Integer> postIndices = userPostIndex.get(searchUser);
 
                         if (postIndices != null && !postIndices.isEmpty()) {
                             for (int index : postIndices) {
                                 posts.get(index).display();
                                 System.out.println();
                             }
                         } 
                         else {
                             System.out.println("No posts found for this username.");
                         }
                         break;
 
                     case 6: //search posts by keyword
                         System.out.println("Enter a keyword to search:");
                         String keyword = scanner.nextLine().toLowerCase();
 
                         boolean postFound = false;
                         for (Post post : posts) {
                             if (post instanceof textPost) {
                                 String content = ((textPost) post).getContent().toLowerCase();
                                 StringTokenizer tokenizer = new StringTokenizer(content);
                                 while (tokenizer.hasMoreTokens()) {
                                     String word = tokenizer.nextToken();
                                     if (word.equals(keyword)) {
                                         post.display();
                                         System.out.println();
                                         postFound = true;
                                         break;
                                     }
                                 }
                             }
                         }
                         if (!postFound) {
                             System.out.println("No posts found containing this keyword.");
                         }
                         break;
 
                     case 7: //save discussion board to file
                         System.out.println("Enter filename to save:");
                         String filename = scanner.nextLine();
                         try (PrintWriter outputStream = new PrintWriter(new FileOutputStream("boards/" + filename + ".dboard"))) {
                             for (Post post : posts) {
                                 outputStream.println("Post #" + post.getPostId());
                                 outputStream.println("Created By: " + post.getUser().getFullName() + " (@" + post.getUser().getUsername() + ")");
                                 outputStream.println("Title: " + post.getTitle());
                                 if (post instanceof textPost) {
                                     outputStream.println(((textPost) post).getContent());
                                 } 
                                 else if (post instanceof pollPost) {
                                     pollPost poll = (pollPost) post;
                                     for (int i = 0; i < poll.getOptions().size(); i++) {
                                         outputStream.println(poll.getOptions().get(i) + ": " + poll.getVotes().get(i));
                                     }
                                 }
                                 outputStream.println();
                             }
                             System.out.println("Discussion board saved successfully!");
                         } 
                         catch (FileNotFoundException e) {
                             System.out.println("Error saving file.");
                         }
                         break;
 
                     case 8: //exit program
                         System.out.println("Exiting program...");
                         scanner.close();
                         return;
 
                     default:
                         System.out.println("Invalid input. Try again.");
                 }
             } 
             catch (Exception e) {
                 System.out.println("Error occurred: " + e.getMessage());
             }
         }
     }
 }
 
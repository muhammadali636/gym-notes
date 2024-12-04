Discussion Board Program
This is a simple program to share and manage messages on a discussion board.

Features:

Post messages (up to 10).
Each message shows the user's name followed by "says:" (e.g., Ali says: Hello!).
View all messages or see them in reverse order.
Search for messages by user name or keyword (case-sensitive or insensitive).
Count the total number of posts and the vowels (a, e, i, o, u) in the messages.
Menu Options:

Post a message
Show all messages
Show messages in reverse order
Show total posts
Search for messages by user
Count vowels
Search for a keyword (case-sensitive)
Search for a keyword (case-insensitive)
Exit the program
How to Compile and Run:

Make sure you have Java 17 installed. Check with java -version.
Compile the program: javac DiscussionBoard.java.
Run the program: java DiscussionBoard.
Details for Posting:

To post a message, users must first register by creating a user profile.
Users enter their full name (which can be multiple words) and a username (which should be one word). The username is optional; if it's not provided, the program will use the first name from the full name in lowercase.
Before adding a new user, the program checks if the username is already taken. If it is, the registration fails.
When creating a post, the user must enter their username first. If the username is not registered, the program will not allow the post.
For the title and content of the post, users enter multiple words on separate lines.
When viewing posts, they are displayed in the order they were made (most recent last).
Notes:

A word is defined as a group of letters and numbers surrounded by spaces.
All posts, full names, and usernames must be entered in single lines.
The program matches keywords case-insensitively but checks whole words only (e.g., "program" and "programming" are not the same).
Sample Menu:

Create a new user
Create a new post
View all posts
View posts by a specific user
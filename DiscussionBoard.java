/*DiscussionBoard v1*/ 

import java.util.Scanner;

public class DiscussionBoard {
   public static void main (String args[]) {
       String name[] = new String[10];
       String post[] = new String[10];
       int count = 0;
       Scanner scanner = new Scanner(System.in);
       int operator = 0;
       int vowel_count = 0;
       String word_search;
   
       while (true) {
           //command loop
           System.out.println("Select one:");
           System.out.println("(1)Post new message");
           System.out.println("(2)Print all posts");
           System.out.println("(3)Print all posts in reverse order");
           System.out.println("(4)Print number of posts entered so far");
           System.out.println("(5)Print all posts from a user");
           System.out.println("(6)Print the number of vowels across all posts");
           System.out.println("(7)Perform a search of posts containing a given word (case sensitive)");
           System.out.println("(8)Perform a search of posts containing a given word (case insensitive)");
           System.out.println("(9)End Program");

           operator = scanner.nextInt(); //for choice selection
           scanner.nextLine(); //consume newline because of int input.

           switch (operator) {
               //post a new message
               case 1:
                   if (count == 10) {
                       System.out.println("Post board is full.");
                   }
                   else {
                       System.out.println("Enter your name: ");
                       name[count] = scanner.nextLine();
                       System.out.println("Enter a new post: ");
                       post[count] = scanner.nextLine();
                       count++;
                   }
                   break;

               //display all posts. 
               case 2:
                   if (count > 0) {
                       for (int i = 0; i < count; i++) {
                           System.out.println(name[i] + " says: " + post[i]);
                       }
                   }
                   else {
                       System.out.println("Nothing has been posted yet!");
                   }
                   break;

               //display all posts in reverse order
               case 3:
                   if (count > 0) {
                       for (int i = count - 1; i >= 0; i--) {
                           System.out.println(name[i] + " says: " + post[i]);
                       }
                   }
                   else {
                       System.out.println("Nothing has been posted yet!");
                   }
                   break;

               //print the number of posts written
               case 4:
                   System.out.println("Number of posts so far: " + count);
                   break;

               //print messages from a particular user. Case insensitive.
               case 5:
                   if (count > 0) {
                       System.out.println("Enter a name whose posts you want to search for: ");
                       String search_name = scanner.nextLine().toLowerCase();
                       for (int i = 0; i < count; i++) {
                           if (name[i].toLowerCase().equals(search_name)) {
                               System.out.println(name[i] + " says: " + post[i]);
                           }
                           else {
                               continue;
                           }
                       }
                       break;
                   }
                   else {
                       System.out.println("Nothing has been posted yet!");
                   }

               //vowel counter Print the number of vowels across all posts" - only count vowels within the post and not the name;
               case 6:
                   int total_vowels = 0;
                   if (count == 0) {
                       System.out.println("Nothing has been posted yet!");
                   }
                   else {
                       for (int i = 0; i < count; i++) {
                           vowel_count = 0;
                           for (int j = 0; j < post[i].length(); j++) {
                               if (post[i].toLowerCase().charAt(j) == 'a' || 
                                   post[i].toLowerCase().charAt(j) == 'e' ||
                                   post[i].toLowerCase().charAt(j) == 'i' ||
                                   post[i].toLowerCase().charAt(j) == 'o' ||
                                   post[i].toLowerCase().charAt(j) == 'u') {
                                       vowel_count++;
                                   }
                           }
                           total_vowels += vowel_count;
                       }
                       System.out.println("Total vowels: " +total_vowels);
                   }
                   break;
                   
               //Perform a search of posts containing a given word (case sensitive
               case 7:
                   System.out.println("Enter a word to search: ");
                   word_search = scanner.nextLine();
                   if (count == 0) {
                       System.out.println("Nothing has been posted yet!");
                   }
                   else {
                       for (int i = 0; i < count; i++) {
                           if (post[i].contains(word_search)) {
                               System.out.println(name[i] + " says: " + post[i]);
                           }
                       }
                   }
                   break;

               //Perform a search of posts containing a given word (case insensitive
               case 8:
                   if (count == 0) {
                       System.out.println("Nothing has been posted yet!");
                   }
                   else {
                       System.out.println("Enter a word to search: ");
                       word_search = scanner.nextLine().toLowerCase();
                       for (int i = 0; i < count; i++) {
                           if (post[i].toLowerCase().contains(word_search)) {
                               System.out.println(name[i] + " says: " + post[i]);
                           }
                       }
                   }
                   break;

               //exit
               case 9:
                   System.out.println("Exiting program...");
                   scanner.close();
                   return;
               
               default: 
                   System.out.println("Invalid input.");
           }
       }
   }
}
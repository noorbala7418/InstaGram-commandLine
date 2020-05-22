package com.noorbala7418.instaghw;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

public class Main {

    public static void main(String[] args) throws InterruptedException, SQLException {
        JDBCHandler jdbcHandler = new JDBCHandler();

        appLogin();
    }

    public static void appLogin() throws InterruptedException {
        Scanner input = new Scanner(System.in);
        JDBCHandler jdbcHandler = new JDBCHandler();
        boolean okLogin = false, okPassword = false;
        User user;
        String username = "", password = "";

        System.out.println("**** WELCOME to InstaGHW ****");
        System.out.println("**** Login Menu ****");
        System.out.print("Enter Username: ");
        username = input.next();
        user = jdbcHandler.dbGetInfo(username);
        System.out.print("Enter Password: ");
        password = input.next();

        okPassword = user.getPassword().equals(password);
        if (okPassword) {
            okLogin = true;
        }
        if (okLogin == true) {
            System.out.println("your Login Success... please wait...");
            Thread.sleep(1500);
            clearScreen();
            mainMenu(user);
        } else {
            //back to this method
            System.out.println("Password is incorrect! please try again");
            appLogin();
        }
    }

    public static void mainMenu(User user) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        Scanner input = new Scanner(System.in);

        System.out.println("***** Welcome to Main Menu *****");
        System.out.println("Enter 1 to show page info");
        System.out.println("Enter 2 to show page posts");
        System.out.println("Enter 3 to show page following");
        System.out.println("Enter 4 to show page followes");
        System.out.println("Enter 0 to quit");
        int option = input.nextInt();
        switch (option) {
            case 1:
                //page info
                clearScreen();
                showInfo(user);

                break;
            case 2:
                //page posts
                clearScreen();
                showPosts(user);
                break;
            case 3:
                //following
                clearScreen();
                showFollowings(user);
                break;
            case 4:
                //followers
                clearScreen();
                showFollowers(user);
                break;
            case 0:
                //exit
                clearScreen();
                System.out.println("Good Bye!");
                Thread.sleep(2000);
                appLogin();
                break;
        }
    }

    public static void showInfo(User user) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();

        user = jdbcHandler.dbGetInfo(user.getUsername());
        System.out.println("***** page info *****");
        System.out.println("your username: " + user.getUsername());
        System.out.println("your userid: " + user.getUid());
        System.out.println("your bio: " + user.getBio());
        System.out.println("your followers count: " + user.getFollower());
        System.out.println("your following count: " + user.getFollowing());
        System.out.println("your posts count: " + user.getPosts());

        Thread.sleep(6000);
        clearScreen();
        mainMenu(user);
    }

    public static void showFollowers(User user) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        User[] users = new User[jdbcHandler.dbGetCountRecord("fer", "uname_fer", user.getUsername())];
        users = jdbcHandler.dbGetFollow("uname_fer", user.getUsername(), "follower");
        Scanner input = new Scanner(System.in);
        int option = 0;
        System.out.println("***** Follower Usernames *****");
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i].getUsername() + ", UserID: " + users[i].getUid());
        }
        System.out.println("***** Selection List for Followings *****");
        System.out.println("Enter following UserID to open it");
        System.out.println("Enter 0 to exit Selection List for Following.");
        option = input.nextInt();
        if (option == 0) {
            clearScreen();
            mainMenu(user);
        } else {
            for (int i = 0; i < users.length; i++) {
                if (option == users[i].getUid()) {
                    clearScreen();
                    showInfoOtherUser(users[i], user);
                }
            }
        }
    }

    public static void showFollowings(User user) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        User[] users = new User[jdbcHandler.dbGetCountRecord("fer", "fer", user.getUsername())];
        Scanner input = new Scanner(System.in);
        int option;
        users = jdbcHandler.dbGetFollow("fer", user.getUsername(), "following");

        System.out.println("***** Following Usernames *****");
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i].getUsername() + ", UserID: " + users[i].getUid());
        }
        System.out.println("***** Selection List for Followings *****");
        System.out.println("Enter following UserID to open it");
        System.out.println("Enter 1 to add following");
        System.out.println("Enter 0 to exit Selection List for Following.");
        option = input.nextInt();
        if (option == 1) {
            clearScreen();
            showAllUsernames(user);
        } else if (option == 0) {
            clearScreen();
            mainMenu(user);
        } else {
            for (int i = 0; i < users.length; i++) {
                if (option == users[i].getUid()) {
                    clearScreen();
                    showInfoOtherUser(users[i], user);
                }
            }
        }
    }

    public static void showInfoOtherUser(User userShower, User userLogger) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        Scanner input = new Scanner(System.in);
        int option;
        userShower = jdbcHandler.dbGetInfo(userShower.getUsername());

        System.out.println("***** page info *****");
        System.out.println("your username: " + userShower.getUsername());
        System.out.println("your userid: " + userShower.getUid());
        System.out.println("your bio: " + userShower.getBio());
        System.out.println("your followers count: " + userShower.getFollower());
        System.out.println("your following count: " + userShower.getFollowing());
        System.out.println("your posts count: " + userShower.getPosts());
        System.out.println();
        System.out.println("****** Selection List for posts *****");
        System.out.println("Enter 1 to show page posts");
        System.out.println("Enter 0 to exit Selection List for posts");

        option = input.nextInt();
        switch (option) {
            case 1:
                showPostOtherUser(userShower, userLogger);
                break;
            case 0:
                showFollowings(userLogger);
                break;
            default:
                System.out.println("Incorrect input.Try Again!");
                showInfoOtherUser(userShower, userLogger);
        }
    }

    public static void showPostOtherUser(User userShower, User userLogger) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        Post[] posts = new Post[jdbcHandler.dbGetCountRecord("posts", "pauth", userShower.getUsername())];
        posts = jdbcHandler.dbGetPost(userShower.getUsername());
        Scanner input = new Scanner(System.in);
        int option;

        System.out.println("***** your post captions and ids *****");
        for (int i = 0; i < posts.length; i++) {
            System.out.println("post" + posts[i].getPostNum() + ":");
            System.out.println("caption: " + posts[i].getPostCap());
            System.out.println("id: " + posts[i].getPostId());
        }
        System.out.println();
        System.out.println("***** selection list for posts *****");
        System.out.println("Enter post id to show post");
        System.out.println("Enter 0 to exit post selection");
        option = input.nextInt();
        if (option == 0) {
            clearScreen();
            showInfoOtherUser(userShower, userLogger);
        }
        clearScreen();
        showSinglePostOtherUser(posts, option, userLogger);
    }

    public static void showSinglePostOtherUser(Post[] posts, int pid, User userLogger) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        int likeCount = 0;
        Scanner input = new Scanner(System.in);
        String option;
        Post post = new Post();
        User user = new User();
        user = jdbcHandler.dbGetInfo(posts[1].getPauth());

        System.out.println("***** Post Info *****");
        for (int i = 0; i < posts.length; i++) {
            if (pid == posts[i].getPostId()) {
                System.out.println("post" + posts[i].getPostNum() + ":");
                System.out.println("caption: " + posts[i].getPostCap());
                System.out.println("id: " + posts[i].getPostId());
                System.out.println("likes: " + posts[i].getPostLikes());
                post = posts[i];
            }
        }
        System.out.println("***** selection list for single post *****");
        System.out.println("Enter L to Like this post");
        System.out.println("Enter E to redirect posts page");
        option = input.next();
        if (option.equals("L")) {
            likeCount = post.getPostLikes();
            likeCount++;
            post.setPostLikes(likeCount);
            if (jdbcHandler.dbUpdateLike(post) == true) {
                System.out.println("Success...Redirecting to PageInfo.");
                Thread.sleep(1500);
                clearScreen();
                showPostOtherUser(user, userLogger);
            } else {
                System.out.println("Fail!...try Again");
                mainMenu(userLogger);
            }
        } else if (option.equals("E")) {
            clearScreen();
            showPostOtherUser(user, userLogger);
        }
    }

    public static void showAllUsernames(User user) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        User[] users = new User[jdbcHandler.dbGetCountUsernames()];
        users = jdbcHandler.dbGetAllUsernames();
        Scanner input = new Scanner(System.in);
        int option;
        System.out.println("***** All Pages *****");
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i].getUsername() + ", UserID: " + users[i].getUid());
        }
        System.out.println();
        System.out.println("***** Selection List For Pages *****");
        System.out.println("Enter Page UserID to Follow");
        System.out.println("Enter 0 to exit AllPages");
        option = input.nextInt();
        if (option == 0) {
            clearScreen();
            showFollowings(user);
        } else if (option == user.getUid()) {
            System.out.println("This UID is For you!...You redirect to List Of All Usernames.");
            Thread.sleep(1500);
            showAllUsernames(user);
        } else {
            for (int i = 0; i < users.length; i++) {
                if (option == users[i].getUid()) {
                    boolean okFollow = jdbcHandler.dbAddFollowing(users[i].getUsername(), user.getUsername());
                    if (okFollow == true) {
                        jdbcHandler.dbUpdateUserStr(user.getUsername());
                        jdbcHandler.dbUpdateUserStr(users[i].getUsername());
                        System.out.println("Success!...You follow this user: " + users[i].getUsername());
                        System.out.println("You will redirect to Main Menu");
                        Thread.sleep(2500);
                        clearScreen();
                        mainMenu(user);
                    } else {
                        System.out.println("Failure!...UserID That You Entered id one of your Followers ...You redirect to List Of All Usernames.");
                        Thread.sleep(1500);
                        showAllUsernames(user);
                    }
                }
            }
        }
    }

    public static void showPosts(User user) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        Post[] posts = new Post[jdbcHandler.dbGetCountRecord("posts", "pauth", user.getUsername())];
        posts = jdbcHandler.dbGetPost(user.getUsername());
        Scanner input = new Scanner(System.in);
        int option;

        System.out.println("***** your post captions and ids *****");
        for (int i = 0; i < posts.length; i++) {
            System.out.println("post" + posts[i].getPostNum() + ":");
            System.out.println("caption: " + posts[i].getPostCap());
            System.out.println("id: " + posts[i].getPostId());
        }
        System.out.println("***** selection list for posts *****");
        System.out.println("Enter post id to show post");
        System.out.println("Enter 1 to add new post");
        System.out.println("Enter 0 to exit post selection");
        option = input.nextInt();
        if (option == 1 || option == 0) {
            clearScreen();
            switch (option) {
                case 1:
                    //add new post method
                    int lastPostId = posts[posts.length - 1].getPostId();
                    int lastPostNumber = posts[posts.length - 1].getPostNum();
                    sendPost(user, lastPostId, lastPostNumber);
                    break;
                case 0:
                    mainMenu(user);
                    break;
            }
        }
        clearScreen();
        showSinglePost(posts, option);
    }

    public static void showSinglePost(Post[] posts, int pid) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        User user = new User();
        user = jdbcHandler.dbGetInfo(posts[1].getPauth());
        System.out.println("***** Post Info *****");
        for (int i = 0; i < posts.length; i++) {
            if (pid == posts[i].getPostId()) {
                System.out.println("post" + posts[i].getPostNum() + ":");
                System.out.println("caption: " + posts[i].getPostCap());
                System.out.println("id: " + posts[i].getPostId());
                System.out.println("likes: " + posts[i].getPostLikes());
            }
        }
        Thread.sleep(6000);
        clearScreen();
        mainMenu(user);
    }

    public static void sendPost(User user, int lastPostID, int lastPostNumber) throws InterruptedException {
        JDBCHandler jdbcHandler = new JDBCHandler();
        Post post = new Post();
        Scanner input = new Scanner(System.in);
        String postCap = "";
        boolean successSend = false;

        System.out.println("***** Send Post *****");
        System.out.print("Enter Your caption(limit:1000 character): ");
        post.setPauth(user.getUsername());
        postCap = input.nextLine();
        post.setPostCap(postCap);
        post.setPostNum(lastPostNumber + 1);
        post.setPostId(lastPostID + 1);
        post.setPostLikes(0);

        successSend = jdbcHandler.dbAddPost(post);
        if (successSend == true) {
            System.out.println("Success!...You Will Redirect to Main Page");
            jdbcHandler.dbUpdateUserStr(user.getUsername());
            Thread.sleep(1500);
            clearScreen();
            mainMenu(user);
        } else {
            System.out.println("Failed!...Please try Again.");
            sendPost(user, lastPostID, lastPostNumber);
        }
    }

    public static void clearScreen() {
        System.out.println("\n\n\n\n\n\n\n\n\n");
    }

    public static int createRandID() {
        Random random = new Random();
        int randID = random.nextInt(10000);
        return randID;
    }
}

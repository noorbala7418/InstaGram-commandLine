package com.noorbala7418.instaghw;


import com.mysql.cj.xdevapi.SqlDataResult;

import java.sql.*;

public class JDBCHandler {
    private Connection connection;
    final private static String dbName;
    final private static String username;
    final private static String password;
    final private static String server;

    static {
        dbName = "insta_db";
        username = "root";
        password = "";
        server = "localhost";
    }

    public JDBCHandler() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/insta_db", username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertUserDB(User user) {
        try {
            //users (username ,uid, Password ,bio,followers,following,posts)
            String query = "insert into users  values (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, user.getUsername());
            statement.setInt(2, user.getUid());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getBio());
            statement.setInt(5, user.getFollower());
            statement.setInt(6, user.getFollowing());
            statement.setInt(7, user.getPosts());

            //System.out.println(statement.execute());
            statement.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            //System.err.println("Got an exception!");
            //System.err.println(e.getMessage());
        }
        return false;
    }


    public boolean dbUpdateUser(User user) {
        int posts = 0, followers = 0, following = 0, res = 0;
        try {

            //getting posts number
            String query = "SELECT COUNT(*) FROM posts WHERE pauth = ?";
            PreparedStatement statement_posts = connection.prepareStatement(query);
            statement_posts.setString(1, user.getUsername());
            ResultSet resultSet = statement_posts.executeQuery();
            resultSet.next();
            posts = resultSet.getInt(1);

            //getting followers count
            query = "SELECT COUNT(*) FROM fer WHERE uname_fer = ?";
            PreparedStatement statement_followers = connection.prepareStatement(query);
            statement_followers.setString(1, user.getUsername());
            resultSet = statement_followers.executeQuery();
            resultSet.next();
            followers = resultSet.getInt(1);

            //getting followings count
            query = "SELECT COUNT(*) FROM fer WHERE fer = ?";
            PreparedStatement statement_following = connection.prepareStatement(query);
            statement_following.setString(1, user.getUsername());
            resultSet = statement_following.executeQuery();
            resultSet.next();
            following = resultSet.getInt(1);

            //updating user information
            query = "UPDATE users SET posts = ? , followers = ? , following = ? WHERE username = ?";
            PreparedStatement statement_update = connection.prepareStatement(query);
            statement_update.setInt(1, posts);
            statement_update.setInt(2, followers);
            statement_update.setInt(3, following);
            statement_update.setString(4, user.getUsername());
            res = statement_update.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean dbUpdateUserStr(String username) {
        int posts = 0, followers = 0, following = 0, res = 0;
        try {

            //getting posts number
            String query = "SELECT COUNT(*) FROM posts WHERE pauth = ?";
            PreparedStatement statement_posts = connection.prepareStatement(query);
            statement_posts.setString(1, username);
            ResultSet resultSet = statement_posts.executeQuery();
            resultSet.next();
            posts = resultSet.getInt(1);

            //getting followers count
            query = "SELECT COUNT(*) FROM fer WHERE uname_fer = ?";
            PreparedStatement statement_followers = connection.prepareStatement(query);
            statement_followers.setString(1, username);
            resultSet = statement_followers.executeQuery();
            resultSet.next();
            followers = resultSet.getInt(1);

            //getting followings count
            query = "SELECT COUNT(*) FROM fer WHERE fer = ?";
            PreparedStatement statement_following = connection.prepareStatement(query);
            statement_following.setString(1, username);
            resultSet = statement_following.executeQuery();
            resultSet.next();
            following = resultSet.getInt(1);

            //updating user information
            query = "UPDATE users SET posts = ? , followers = ? , following = ? WHERE username = ?";
            PreparedStatement statement_update = connection.prepareStatement(query);
            statement_update.setInt(1, posts);
            statement_update.setInt(2, followers);
            statement_update.setInt(3, following);
            statement_update.setString(4, username);
            res = statement_update.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
        return false;
    }

    public User dbGetInfo(String username) {
        User user = new User();
        //before select,we have to update user details
        boolean resUpdate = dbUpdateUserStr(username);
        if (resUpdate == true) {
            try {
                String query = "SELECT * FROM users WHERE username = ? ";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    user.setUsername(resultSet.getString("username"));
                    user.setUid(resultSet.getInt("uid"));
                    user.setPassword(resultSet.getString("Password"));
                    user.setBio(resultSet.getString("bio"));
                    user.setFollower(resultSet.getInt("followers"));
                    user.setFollowing(resultSet.getInt("following"));
                    user.setPosts(resultSet.getInt("posts"));
                }
                return user;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    public Post[] dbGetPost(String username) {
        Post[] posts;
        int countPosts = 0;
//before select,we have to update user details
        boolean resUpdate = dbUpdateUserStr(username);
        if (resUpdate == true) {
            try {
//count number of posts that belongs username
                countPosts = dbGetCountRecord("posts", "pauth", username);
                posts = new Post[countPosts];
//now we insert posts in post arr
                String query = "SELECT * FROM posts WHERE pauth = ? ";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                int i = 0;
                while (resultSet.next() && i < countPosts) {
                    posts[i] = new Post();
                    posts[i].setPauth(resultSet.getString(1));
                    posts[i].setPostNum(resultSet.getInt(2));
                    posts[i].setPostCap(resultSet.getString(3));
                    posts[i].setPostId(resultSet.getInt(4));
                    posts[i].setPostLikes(resultSet.getInt(5));
                        /*System.out.println(resultSet.getString(1));
                        System.out.println(resultSet.getInt(2));
                        System.out.println(resultSet.getString(3));
                        System.out.println(resultSet.getInt(4));
                        System.out.println(resultSet.getInt(5));*/
                    i++;
                }
                return posts;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    public int dbGetCountRecord(String TableName, String columnName, String username) {
        int countRecord = 0;
        boolean isUpdate = false;
        //first we update user details
        isUpdate = dbUpdateUserStr(username);
        if (isUpdate == true) {
            try {
                String query = "SELECT COUNT(?) FROM " + TableName + " WHERE " + columnName + " = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, username);
                //System.out.println(statement);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                countRecord = resultSet.getInt(1);

                return countRecord;
            } catch (SQLException e) {
                e.printStackTrace();
                //System.out.println(e.getMessage());
            }
            return 888;
        }
        return 999;
    }

    public boolean dbAddPost(Post post) {
        try {
            String query = "INSERT INTO posts (pauth , pnum , pcap , pid , plike) VALUES (? , ? , ? , ? , ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, post.getPauth());
            statement.setInt(2, post.getPostNum());
            statement.setString(3, post.getPostCap());
            statement.setInt(4, post.getPostId());
            statement.setInt(5, post.getPostLikes());
            statement.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User[] dbGetFollow(String columnName, String username , String modeTarget) {
        User[] users;
        boolean isUpdate = false;
        //first , we update user follow(er/ing)s
        isUpdate = dbUpdateUserStr(username);
        if(modeTarget.equals("follower")){
            modeTarget = "fer";
        }else if(modeTarget.equals("following")){
            modeTarget = "uname_fer";
        }
        if (isUpdate == true) {
            try {
                //count records for initializig users arr
                users = new User[dbGetCountRecord("fer", columnName, username)];
                //now we have to get follow(er/ing)s from db
                String query = "SELECT * FROM fer WHERE " + columnName + "= ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                int i = 0;
                while (resultSet.next() && i < users.length) {
                    users[i] = new User();
                    users[i].setUsername(resultSet.getString(modeTarget));
                    users[i] = dbGetInfo(users[i].getUsername());
                    dbUpdateUser(users[i]);
                    i++;
                }
                return users;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    public User[] dbGetAllUsernames() {
        int countRecord = 0;
        User[] users;
        try {
            //first , count the records
            countRecord = dbGetCountUsernames();

            //now we get users details
            users = new User[countRecord];
            String query = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int i = 0;
            while (resultSet.next() && i < countRecord) {
                users[i] = new User();
                users[i].setUsername(resultSet.getString("username"));
                users[i] = dbGetInfo(users[i].getUsername());
                dbUpdateUser(users[i]);
                i++;
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int dbGetCountUsernames() {
        int countRecord = 0;
        try {
            String query = "SELECT COUNT(*) FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            countRecord = resultSet.getInt(1);

            return countRecord;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean dbAddFollowing(String uname_fer, String fer) {
        boolean isFollowing = false;

        //we check that fer is follower of uname_fer or now?
        isFollowing = dbCheckFollowing(uname_fer, fer);

        if (isFollowing == true) {//it means that this record is not available
            try {
                String query = "INSERT INTO fer (uname_fer , fer) VALUES (?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, uname_fer);
                statement.setString(2, fer);
                statement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    public boolean dbCheckFollowing(String uname_fer, String fer) {
        int rec = 0;
        try {
            String query = "SELECT COUNT(?) FROM fer WHERE uname_fer = ? AND fer = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, uname_fer);
            statement.setString(2, uname_fer);
            statement.setString(3, fer);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            rec = resultSet.getInt(1);
            if (rec == 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean dbUpdateLike(Post post){
        int res = 0;
        try {
            String query = "UPDATE posts SET plike = ? WHERE pauth = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,post.getPostLikes());
            statement.setString(2,post.getPauth());
            res = statement.executeUpdate();
            if (res == 1) {
                dbUpdateUserStr(post.getPauth());
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
package com.example.fawad.bingochat;


import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fawad.bingochat.contact.Contact;
import com.example.fawad.bingochat.message.Message;
import com.example.fawad.bingochat.post.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Created by Fawad on 11/26/2017.
 */

public class Utils {
    public static void WriteUser(String name,String email,String uid,String avatar){
        User user=new User(name,email,uid,avatar);
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .setValue(user);

    }
    public static void WriteContact(String currentUserID, Contact contact, final View view){
        FirebaseDatabase.getInstance()
                .getReference("contacts")
                .child(currentUserID)
                .push()
                .setValue(contact, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            createSnackbar(view,"Added Successfully","OK",Snackbar.LENGTH_SHORT,"#90EE90");
                        }
                        else{
                            createSnackbar(view,databaseError.getMessage(),"ERROR",Snackbar.LENGTH_SHORT,"#FF5252");
                        }
                    }
                });
    }

    public static void FindAndWriteContact(final String currentUserId, final String email,final View view){

        //value event of firebase for reading value once
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if there is data
                if(dataSnapshot.hasChildren()){
                    //get list of data on that node 'users' and loop through it
                    for (DataSnapshot snap:dataSnapshot.getChildren()){
                        User user=snap.getValue(User.class);
                        //if the user is register with that email in our app
                        //and doesnot try to add himself
                        if(!(user.getUid().equals(currentUserId)) && user.getEmail().equals(email)){
                            //create contact
                            Contact contact=new Contact(user.getName(),user.getEmail(),user.getUid(),user.getAvatar());
                            //write in the contact list of the current user
                            WriteContact(currentUserId,contact,view);
                            return;
                        }
                    }
                    createSnackbar(view,"Email: '"+email+"'","NOT FOUND",Snackbar.LENGTH_LONG,"#FF5252");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // if some thing gone wrong
            }
        });
    }
    // this method is not for "chat" node  but for a seperate node called "messages"
    // but will be invoke when ever user send message to other user
    // so messages tab can read the last message send by any user
    // note: there are two node  "chats" and "messages" which contain same data only difference is
    // chat contain all messages and message node contain only last message
    public static void WriteMessage(String to_uid,String from_uid,Message msg){
        FirebaseDatabase.getInstance()
                .getReference("messages")
                .child(to_uid)
                .child(from_uid)
                .setValue(msg);

    }
    public static UploadTask UploadImage(Uri Image, String UserId){
        return FirebaseStorage.getInstance()
                .getReference()
                .child("images").child(UserId+"/"+Image.getLastPathSegment())
                .putFile(Image);
    }

//    public static void WritePosts(final FirebaseUser user, final String title, final String summary, Uri Image, final View view, final ProgressBar progress){
//        UploadImage(Image,user.getUid()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Post post=new Post(user.getDisplayName(),
//                        user.getUid(),
//                        user.getEmail(),
//                        user.getPhotoUrl().toString(),
//                        title,
//                        summary,
//                        taskSnapshot.getDownloadUrl().toString());
//
//                FirebaseDatabase.getInstance()
//                        .getReference("posts")
//                        .child(user.getUid())
//                        .setValue(post, new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                if(databaseError==null){
//                                    createSnackbar(view,"Added Successfully","OK",Snackbar.LENGTH_SHORT,"#90EE90");
//                                }
//                            }
//                        });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progressNum = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                progress.setProgress((int)progressNum);
//
//            }
//        });
//    }
    public static void WritePost(Post post, String uid, final View view){
        FirebaseDatabase.getInstance().getReference("posts").child(uid).setValue(post, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){
                    createSnackbar(view,"Added Successfully","OK",Snackbar.LENGTH_SHORT,"#90EE90");
                }
            }
        });
    }
    static void createSnackbar(View view,String message,String actionMessage,int length,String hexacode){
        Snackbar.make(view,message,length)
                .setActionTextColor(Color.parseColor(hexacode))
                .setAction(actionMessage,new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        //do nothing
                    }
                }).show();

    }
    //juggar method
    // to create a key with two input and only one output without worrying about input sequence
    //for example 2+3=6 and 3+2=6 output will always be 6
    public static String createJoinedKey(String uid1,String uid2){
        byte[] temp1=uid1.substring(0,11).getBytes(StandardCharsets.US_ASCII);
        int key1=new BigInteger(temp1).intValue();
        byte[] temp2=uid2.substring(0,11).getBytes(StandardCharsets.US_ASCII);
        int key2=new BigInteger(temp2).intValue();
        return key1+key2+"";

    }
}

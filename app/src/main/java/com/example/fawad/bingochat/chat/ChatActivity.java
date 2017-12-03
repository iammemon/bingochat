package com.example.fawad.bingochat.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fawad.bingochat.MainActivity;
import com.example.fawad.bingochat.R;
import com.example.fawad.bingochat.Utils;
import com.example.fawad.bingochat.message.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class ChatActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private String to_uid;
    private TextView title;
    private ImageView avatarImage;
    private RecyclerView mRecyclerView;
    private EditText editText;
    private String ReceiverUId;
    private String JOINEDKEY;
    private static FirebaseUser currentUser;
    //private static final Query mChatQuery=FirebaseDatabase.getInstance().getReference().child("messages").child(R);
    private static Query mChatQuery;
//            FirebaseDatabase.
//                    getInstance().
//                    getReference().
//                    child("messages").
//                    child(currentUser.getUid())
//                    .limitToLast(50);

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        //getting data from intent
        Intent intent=getIntent();
        String avatar=intent.getStringExtra("AVATAR");
        String uid=intent.getStringExtra("UID");
        String name =intent.getStringExtra("NAME");
        to_uid=uid;
        //unique key for chat between two users
        //JOINEDKEY=currentUser.getUid().substring(0,6)+uid.substring(0,6);
        //JOINEDKEY=createJoinedkey(currentUser.getUid(),uid);
        JOINEDKEY= Utils.createJoinedKey(currentUser.getUid(),uid);
        //ReceiverUId=uid;
        mChatQuery= FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("chats")
                                    .child(JOINEDKEY)
                                    .limitToLast(50);

        title=(TextView) findViewById(R.id.chat_title);
        avatarImage=(ImageView) findViewById(R.id.chat_avatar);
        title.setText(name);
        Glide.with(getApplicationContext()).load(avatar).into(avatarImage);
        editText=(EditText)findViewById(R.id.writeMessage);
        mRecyclerView=(RecyclerView)findViewById(R.id.message_recycler_view);
        attachRecyclerViewAdapter();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ImageView back = (ImageView)findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public String createJoinedkey(String currentUser,String Otheruser){
        byte[] current=currentUser.substring(0,7).getBytes(StandardCharsets.US_ASCII);
        int a=new BigInteger(current).intValue();
        byte[] other=Otheruser.substring(0,7).getBytes(StandardCharsets.US_ASCII);
        int b=new BigInteger(other).intValue();
        return a+b+"";
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        //todo authstate wala kam baki he
    }

    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        mRecyclerView.setAdapter(adapter);
    }
    public void SendMessage(View view){

        String uid=currentUser.getUid();
        String name=currentUser.getDisplayName();
        String msg=editText.getText().toString();
        String avatar=currentUser.getPhotoUrl().toString();
        String timestamp=""+System.currentTimeMillis();
        addMessage(new Chat(name,uid,avatar,msg,timestamp));

        editText.setText("");
    }

    protected RecyclerView.Adapter newAdapter() {

        FirebaseRecyclerOptions<Chat> options =
                new FirebaseRecyclerOptions.Builder<Chat>()
                        .setQuery(mChatQuery, Chat.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirebaseRecyclerAdapter<Chat, ChatHolder>(options) {
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ChatHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_message, parent, false));
            }

            @Override
            protected void onBindViewHolder(ChatHolder holder, int position, Chat model) {
                holder.Bind(model);
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
            }


        };
    }

    private void addMessage(Chat chat){
        mChatQuery.getRef().push().setValue(chat, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference reference) {
                if (error != null) {
                    Log.e("Message Failed", "Failed to write message", error.toException());
                }
            }
        });

        Message msg=new Message(chat.getName(),chat.getMessage(),chat.getUid(),chat.getAvatar());
        Utils.WriteMessage(to_uid,currentUser.getUid(),msg);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

}

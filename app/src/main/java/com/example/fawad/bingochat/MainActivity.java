package com.example.fawad.bingochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fawad.bingochat.contact.ContactFragment;
import com.example.fawad.bingochat.message.MessageFragment;
import com.example.fawad.bingochat.post.PostFragment;
import com.example.fawad.bingochat.post.PostFragmentDialog;
import com.example.fawad.bingochat.setting.SettingActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    //use by firebase auth ui
    private static final int RC_SIGN_IN = 1;
    private static final int PICK_IMAGE = 10;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //track userid
    private String CurrentUserID;
    //use to track tabs
    private int currentTab=0;
    // ui components
    private FloatingActionButton fab;
    private TabLayout tabs;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init views
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (TabLayout) findViewById(R.id.tabs);


        setSupportActionBar(toolbar);
        //custom method
        //setupViewPager(viewPager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //custom method define in class
                onFabClick(v);
            }
        });
        //event when tab changed
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //custom method define in class
                onTabChanged(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //lol
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //lol
            }
        });
        //tabs.setupWithViewPager(viewPager);


        //Firebase Authorization Initialize
        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                onAuthChanged(firebaseAuth.getCurrentUser());
            }
        };

    }


    private void onAuthChanged(FirebaseUser user){
        if(user!=null){
            // user is signed in
            setupViewPager(viewPager);
            tabs.setupWithViewPager(viewPager);
            // entry in database if user signIn
            CurrentUserID=user.getUid();
            Utils.WriteUser(user.getDisplayName(), user.getEmail(), user.getUid(),user.getPhotoUrl().toString());
            Toast.makeText(MainActivity.this,"You are now signed In",Toast.LENGTH_SHORT).show();
        }
        else{
            // user is signed out
            //if signed out show signIn screen
            createSignIn();
        }
    }
    //this will create authorization flow provided by firebase auth ui
    //Note: The UI for signIn will be provided by lib therefore there won,t be any xml file in resources dir
    private void createSignIn(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.logo)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);


    }

    private void onTabChanged(int tabPosition){
        //change fab icon when tab changed and keep track using currentTab variable
        switch (tabPosition){
            case 0:
                fab.setVisibility(View.GONE);
                //fab.setImageResource(R.drawable.ic_message_white_24dp);
                currentTab=0;
                break;
            case 1:
                fab.setVisibility(View.VISIBLE);
                fab.setImageResource(R.drawable.ic_add_white_24dp);
                currentTab=1;
                break;
            case 2:
                fab.setVisibility(View.VISIBLE);
                fab.setImageResource(R.drawable.ic_person_add_white_24dp);
                currentTab=2;
                break;
            default:
                return;
        }
    }

    private void onFabClick(View v){
        //if tab is currently on "contact" create dialog to add contact
        if(currentTab==2){
            createDialog(v);

        }
        else if(currentTab==1){
            FragmentManager fm = getSupportFragmentManager();
            PostFragmentDialog dialog=new PostFragmentDialog();
            dialog.show(fm,"Post Dialog");

            //createPostDialog();
        }
        else{
            Snackbar.make(v,"Hello SnackBar",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createDialog(final View view){
        //custom layout for dialog
        final LayoutInflater inflater =getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.contact_dialog,null);
        //creating dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add Contact");
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText text=dialogView.findViewById(R.id.add_contact_edittext);
                Utils.FindAndWriteContact(CurrentUserID,text.getText().toString(),view);
            }
        });
        builder.show();


    }
    private void createPostDialog(){
//        final Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        final Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //custom layout for dialog
        final LayoutInflater inflater =getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.post_dialog,null);
        Button btn=dialogView.findViewById(R.id.uploadImageBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    EditText text = (EditText) dialogView.findViewById(R.id.editText);
//                    ImageView img=(ImageView) dialogView.findViewById(R.id.imagePreview);
//                    img.setImageResource(R.drawable.a);
//                    text.setText("helllo");
//                }
//                catch (Exception e){
//                    Log.e("khtra",e.getMessage());
//                }
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                startActivityForResult(i, PICK_IMAGE);

            }
        });
        //creating dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add Post");
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //EditText text=dialogView.findViewById(R.id.add_contact_edittext);
            }
        });
        builder.show();
    }
    public void PickImageFromGallery(){
        final Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);
    }
    //custom method to attach fragments with adapter
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new MessageFragment(), "Messages");
        adapter.addFragment(new PostFragment(), "Posts");
        adapter.addFragment(new ContactFragment(),"Contacts");
        viewPager.setAdapter(adapter);

    }

    //this adapter will be use by viewpager which eventually will be use in tabs
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //bug solved  when pressing back button on sign in page :)
        if(requestCode==RC_SIGN_IN && resultCode==RESULT_CANCELED){
            finish();
        }
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
//            Log.e("insideactivity","agaya");
//            Fragment f=getSupportFragmentManager().findFragmentByTag("Post Dialog");
//            f.onActivityResult(requestCode,resultCode,data);
//            try {
//              //  final LayoutInflater inflater =getLayoutInflater();
//               // final View postdialog=inflater.inflate(R.layout.post_dialog,null);
////                ImageView imgview = (ImageView) view.findViewById(R.id.imagePreview);
////                Uri img = data.getData();
////                Log.i("imagepath",img.getPath());
////                if(imgview!=null){
////                    Log.e("debug","no error");
////                    imgview.setImageResource(R.drawable.a);
////                    EditText text =(EditText) view.findViewById(R.id.editText);
////                    text.setText(img.getPath());
////
////                }
//               // Glide.with(view.getContext()).load(R.id.dr).into(imgview);
//                Uri selectedImage = data.getData();
//                final LayoutInflater inflater=getLayoutInflater();
//                View meraview=inflater.inflate(R.layout.post_dialog,null);
//                EditText tex=(EditText)meraview.findViewById(R.id.editText);
//                tex.setText("hello");
//            }catch (Exception e){
//                Log.e("ImageError",e.getMessage());
//            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            // firebase auth ui method to signout
            AuthUI.getInstance().signOut(this);
            return true;
        }
        int id2 = item.getItemId();
         if (id2 == R.id.settings){
            Intent intent = new Intent(this, SettingActivity.class);
             startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}

package gaongil.safereturnhome.scene;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import gaongil.safereturnhome.R;
import gaongil.safereturnhome.adapter.ChatAdapter;
import gaongil.safereturnhome.fragment.ChatLeftDrawerFragment;
import gaongil.safereturnhome.fragment.ChatRightDrawerFragment;
import gaongil.safereturnhome.model.MessageData;
import gaongil.safereturnhome.model.MessageType;
import gaongil.safereturnhome.support.DrawableListener;

public class ChatActivity extends ActionBarActivity implements OnClickListener {
	
	/************************************************************************
	 * Forground Check
	 */
	private static boolean VISIBILITY = false;
	public static boolean isVisible() {
		return ChatActivity.VISIBILITY;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ChatActivity.VISIBILITY = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		ChatActivity.VISIBILITY = false;
	}
	/**
	 * Forground Check
	 *************************************************************************/
	
	/**
	 * MainContents
	 */
	// Message List
	private ArrayList<MessageData> mMessageList;
	// Chat Custom Adapter.
	private ChatAdapter mChatAdapter;
	// EditText to compose the messages
	private EditText mEditText;
	// Button to submit new message
	private Button mBtnSend;
	// Control Keyboard Panel
	private InputMethodManager inputMethodManager;
	private ActionBarDrawerToggle mDrawerToggle;


	/**
	 * The drawer layout
	 */
	private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;

	/**
	 * Left drawer
	 */
	private View mLeftDrawerView;

	/**
	 * Right drawer
	 */
	private View mRightDrawerView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_chat);
	    loadMessageList();
	    init();
	    
	    setupActionBar("BF");
	    setupDrawer();
	    
	    //TODO DELETE
	    //Test Start
	    setupTest();
	  //TODO DELETE
	    //Test End
	}

	//TODO DELETE
    //Test Start
	private void setupTest() {
		mMessageList.add(new MessageData(0, 0, "[귀가시작] 집까지 같이해 주세요.위치정보, 센서정보 공유가 시작되었습니다.\n", new Date(1,1,1, 20, 51),  MessageType.ANNOUNCE, true));
		mMessageList.add(new MessageData(0, 0, "[충격 위협감지] 갑작스러운\n 충격이 감지되었습니다.\n위치 : 성남시 분당구 삼평동 봇들공원\n !무슨일인지 확인해주세요!", new Date(1,1,1, 21, 03),  MessageType.URGENT, true));
		mMessageList.add(new MessageData(0, 1, "무슨일이야??", new Date(1,1,1, 21, 03),  MessageType.NORMAL, false));
		mMessageList.add(new MessageData(0, 4, "내가 전화해볼게", new Date(1,1,1, 21, 04),  MessageType.ANNOUNCE, false));
		mMessageList.add(new MessageData(0, 0, "아.. 괜찮아! 딴데보다가 살짝 넘어졌어ㅠ", new Date(1, 1, 1, 21, 04),  MessageType.NORMAL, true ));
		mMessageList.add(new MessageData(0, 3, "으이구.. 괜찮아?\n데릴러 나갈게", new Date(1, 1, 1, 21, 04),  MessageType.NORMAL, false));
		mMessageList.add(new MessageData(0, 0, "응응! 고마워어ㅠㅠㅠ", new Date(1, 1, 1, 21, 05),  MessageType.NORMAL, true ));
		
		mChatAdapter.notifyDataSetChanged();

	}
	//TODO DELETE
    //Test End
	
	private void setupActionBar(String groupName) {
		centerActionBarTitle(groupName);
	}
	
	private void centerActionBarTitle(String groupName) {

		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null)
			return;
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(groupName);
		
        int titleId = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        }
        else
        {
            // This is the id is from your app's generated R class when ActionBarActivity is used 
            // for SupportActionBar
            //titleId = R.id.action_bar_title;
        }

        // Final check for non-zero invalid id
        if (titleId > 0)
        {
            TextView titleTextView = (TextView) findViewById(titleId);
            DisplayMetrics metrics = getResources().getDisplayMetrics();

            // Fetch layout parameters of titleTextView (LinearLayout.LayoutParams : Info from HierarchyViewer)
            LinearLayout.LayoutParams txvPars = (android.widget.LinearLayout.LayoutParams) titleTextView.getLayoutParams();
            txvPars.gravity = Gravity.CENTER_HORIZONTAL;
            txvPars.width = metrics.widthPixels;
            titleTextView.setLayoutParams(txvPars);

            titleTextView.setGravity(Gravity.CENTER);
        }
    }

	private void init() {
		 // Chatting ListView Setting
	    ListView list = (ListView)findViewById(R.id.chat_list);
	    mChatAdapter = new ChatAdapter(ChatActivity.this, mMessageList);
	    
	    list.setAdapter(mChatAdapter);
	    list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	    list.setStackFromBottom(true);
	    
	    // Chat Input Box Init
	    mEditText = (EditText) findViewById(R.id.chat_edt);
	    
	    // Submit Button init
	    mBtnSend = (Button) findViewById(R.id.chat_btn_send);
	    mBtnSend.setOnClickListener(this);
	    
	    // inputMethodManager init
	    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void loadMessageList() {
		
		// TODO getDataFrom Server or Local DB
		mMessageList = new ArrayList<MessageData>();
		
		mMessageList.add(new MessageData(
				1, 1, "test", new Date(),  MessageType.NORMAL, true 
		));
	}

	private void sendMessage() {
		
		if (mEditText.length() == 0)
			return;
		
		inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
		
		//TODO getGroupId & UserId & MessageType & etc..
		MessageData newMessage = new MessageData(
				1, 
				1, 
				mEditText.getText().toString(), 
				new Date(), 
				MessageType.NORMAL, 
				false
		);
		
		//TODO Post Message, and Send Success, then add chatList
		mMessageList.add(newMessage);
		
		mChatAdapter.notifyDataSetChanged();
		mEditText.setText(null);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_btn_send:
			sendMessage();
			break;
		}
	}

	protected void setupDrawer() {

        mFragmentManager = getSupportFragmentManager();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_chat_layout);
		mLeftDrawerView = (View) findViewById(R.id.drawer_chat_left);
		mRightDrawerView = (View) findViewById(R.id.drawer_chat_right);
		LinearLayout mainContentLayout = (LinearLayout) findViewById(R.id.chat_content_layout);

        DrawerLayout.DrawerListener drawerListener = new DrawableListener(findViewById(R.id.main_content_layout), mDrawerLayout, mLeftDrawerView, mRightDrawerView);

        mDrawerLayout.setDrawerListener(drawerListener);

        //left fragment
        ChatLeftDrawerFragment leftDrawerFragment = new ChatLeftDrawerFragment();

        //right fragment
        ChatRightDrawerFragment rightDrawerFragment = new ChatRightDrawerFragment();

        //fragment transaction
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.drawer_chat_left, leftDrawerFragment);
        fragmentTransaction.replace(R.id.drawer_chat_right, rightDrawerFragment);

        fragmentTransaction.commit();
    }
}
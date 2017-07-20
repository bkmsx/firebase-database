package asia.trente.firebasedatabase;

import android.databinding.DataBindingUtil;
import android.net.LinkAddress;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.trente.firebasedatabase.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{

	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

		binding.btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				UserModel user = new UserModel();
				user.name = binding.edtName.getText().toString();
				user.group = binding.edtGroup.getText().toString();
				String key = database.child("users").push().getKey();
				database.child("users").child(key).setValue(user);
			}
		});

		database.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot){
				FileModel userModel = dataSnapshot.child("files").child("1").getValue(FileModel.class);
				log(userModel.name + " : " + userModel.url);
				new DownloadTask(MainActivity.this).execute(userModel.url);
			}

			@Override
			public void onCancelled(DatabaseError databaseError){

			}
		});
	}

	private void log(String msg){
		Log.e("MainActivity", msg);
	}
}

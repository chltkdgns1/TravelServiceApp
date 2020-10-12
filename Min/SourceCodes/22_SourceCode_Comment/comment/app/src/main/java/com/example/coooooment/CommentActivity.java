package com.example.coooooment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentActivity extends AppCompatActivity {

    Button Button_comment;
    EditText EditText_comment;
    DatabaseReference databaseReference;

    FirebaseRecyclerOptions<User> options;
    FirebaseRecyclerAdapter<User, MyViewHolder> adapter;
    RecyclerView recyclerView;

    String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        EditText_comment = findViewById(R.id.EditText_comment);
        Button_comment = findViewById(R.id.Button_comment);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child("a").child("b").child("comment");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = EditText_comment.getText().toString();
                User user = new User("wongi",comment,"20201009");
                databaseReference.push().setValue(user);
            }
        });

        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(databaseReference, User.class).build();
        adapter = new FirebaseRecyclerAdapter<User, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull User model) {
                holder.TextView_comment.setText(model.getComment());
                holder.TextView_name.setText(model.getName());
                holder.TextView_time.setText(model.getTime());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_list, parent, false);
                return new MyViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
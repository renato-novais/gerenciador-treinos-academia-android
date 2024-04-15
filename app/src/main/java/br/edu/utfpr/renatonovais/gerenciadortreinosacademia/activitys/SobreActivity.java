package br.edu.utfpr.renatonovais.gerenciadortreinosacademia.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.R;

public class SobreActivity extends AppCompatActivity {

    public static void sobre(AppCompatActivity activity){

        Intent intent = new Intent(activity, SobreActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        setTitle(R.string.sobre);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home){
//            finish();
//            return true;
//        }else{
//            return super.onOptionsItemSelected(item);
//        }
//    }
}
package com.adanibo.istgsample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.instagram.instagramapi.activities.InstagramAuthActivity;
import com.instagram.instagramapi.engine.InstagramEngine;
import com.instagram.instagramapi.engine.InstagramKitConstants;
import com.instagram.instagramapi.objects.IGSession;
import com.instagram.instagramapi.utils.InstagramKitLoginScope;

import java.io.File;

public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout istgScn = (LinearLayout) findViewById(R.id.instagram_login);
        istgScn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] scopes = {InstagramKitLoginScope.BASIC, InstagramKitLoginScope.COMMENTS};

                Intent intent = new Intent(MainActivity.this, InstagramAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);

                intent.putExtra(InstagramEngine.TYPE, InstagramEngine.TYPE_LOGIN);
                //add scopes if you want to have more than basic access
                intent.putExtra(InstagramEngine.SCOPE, scopes);

                startActivityForResult(intent, 0);
            }
        });

        LinearLayout istgshrScn = (LinearLayout) findViewById(R.id.instagram_share);
        istgshrScn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "image/*";
                String filename = "/DCIM/instagram_icon48.png";
                String mediaPath = Environment.getExternalStorageDirectory() + filename;

                createInstagramIntent(type, mediaPath);

            }
        });

    }

    private void createInstagramIntent(String type, String mediaPath){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:

                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    if (bundle.containsKey(InstagramKitConstants.kSessionKey)) {
                        IGSession session = (IGSession) bundle.getSerializable(InstagramKitConstants.kSessionKey);
                        Toast.makeText(MainActivity.this, "Woohooo!!! User trusts you :) "
                                + session.getAccessToken(),
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

}

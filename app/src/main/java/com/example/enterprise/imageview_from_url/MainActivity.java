package com.example.enterprise.imageview_from_url;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.InputStream;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the widgets reference from XML layout
        final ImageView iv = (ImageView) findViewById(R.id.iv);
        Button btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){ // Check internet connection
                    //URl of image to display in ImageView
                    String urlOfImage = "https://www.google.com.bd/images/srpr/logo11w.png";

                    // Execute new task to download an image from web and set as ImageView image source
                    new ImageDownloadTask(iv).execute(urlOfImage);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Custom method to check available active network
    public boolean isNetworkConnected(){
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        Boolean result = true;
        if(nInfo == null)
        {
            //There are no active network.
            result = false;
        }
        return result;
    }

    /*
        New private class to download an image from web.
        AsyncTask enables proper and easy use of the UI thread. This class allows to perform
        background operations and publish results on the UI thread without having
        to manipulate threads and/or handlers.
     */
    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap>{

        //Initialize an ImageView Class/widget
        ImageView imageView;

        public ImageDownloadTask(ImageView iv){
            //Specify the initialized ImageView is same as the method calling ImageView
            this.imageView=iv;
        }

        //Background task to download image as bitmap
        protected Bitmap doInBackground(String... urls){
            String urlToDisplay = urls[0];
            Bitmap bmp = null;
            try{
                //Try to download the image from web as stream
                InputStream inputStream = new java.net.URL(urlToDisplay).openStream();
                //decodeStream(InputStream is) method decode an input stream into a bitmap.
                bmp = BitmapFactory.decodeStream(inputStream);

            }catch(Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        //do something with the result
        protected void onPostExecute(Bitmap result)
        {
            //Specify ImageView image source from downloaded image
            imageView.setImageBitmap(result);
        }
    }
}

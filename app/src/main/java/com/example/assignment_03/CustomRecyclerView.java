package com.example.assignment_03;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.ViewHolder> {

    Context context;
    String baseurl = "https://raw.githubusercontent.com/revolunet/PythonBooks/master/";
    private ArrayList<String> title;
    private ArrayList<String> level;
    private ArrayList<String> info;
    private ArrayList<String> imgid;
    private ArrayList<String> url;
    ProgressDialog progressDialog;
    private  String bookUrl="";
    public CustomRecyclerView(Context context, ArrayList<String> title, ArrayList<String> level,ArrayList<String> info, ArrayList<String> imgid, ArrayList<String> url) {

        this.context = context;
        this.title = title;
        this.level = level;
        this.info=info;
        this.imgid = imgid;
        this.url=url;
        progressDialog=new ProgressDialog(this.context);
        progressDialog.setMessage("Downloading....");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_title.setText(title.get(position));
        holder.tv_level.setText(level.get(position));
        holder.tv_info.setText(info.get(position));
        Picasso.get().load(baseurl + imgid.get(position)).into(holder.iv_icon);
        final String subs=url.get(position);
        if(subs.contains("pdf")|| subs.contains("zip"))
        {
            holder.btn.setText("Download");
        }
        else
        {
            holder.btn.setText("Read Onlne");

        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(subs.contains("pdf")|| subs.contains("zip")) {
                    bookUrl=title.get(position);
                    new CustomDownload().execute(subs);
                }
                else
                {
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(subs));
                    context.startActivity(intent);
                }
            }

        });


    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_level;
        public TextView tv_info;
        public ImageView iv_icon;
        public Button btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_level=itemView.findViewById(R.id.tv_level);
            tv_info=itemView.findViewById(R.id.tv_info);
            iv_icon = itemView.findViewById(R.id.iv_img);
            btn=itemView.findViewById(R.id.btn);
        }
    }
class CustomDownload extends AsyncTask<String,Integer,String>
{
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog.show();
    }
    @Override
    protected String doInBackground(String... f_url)
    {
        int count;
        try {
           // Log.e("url-",f_url[0]);

            URL url=new URL(f_url[0]);
            URLConnection conn=url.openConnection() ;
            conn.connect();
            int length=conn.getContentLength();
            InputStream is= new BufferedInputStream(url.openStream());
            final  int PERMISSION_REQUEST_CODE=12345;
            ActivityCompat.requestPermissions((Activity)context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            OutputStream outputStream=new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+bookUrl+".pdf");
            byte[]buffer=new byte[1024];
            long total=0;
            while ((count=is.read(buffer))!=-1)
            {
                total+=count;
                publishProgress((int)((total*100)/length));
                outputStream.write(buffer,0,count);
            }
            outputStream.flush();;
            outputStream.close();;
            is.close();
            is.close();


        }
        catch (FileNotFoundException e)
        {

        }
        catch (IOException e)
        {

        }
        return  null;

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
            if(o!=null)
            {
                Toast.makeText(context,"Download Error"+o,Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();

            }
    }
}

}

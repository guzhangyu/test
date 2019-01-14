package interview;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class CurlTest {

    public static void main(String[] args) throws IOException {
        System.out.println("dsd");
        while(true){

        }
//        HttpClient httpClient=HttpClientBuilder.create().build();
//        HttpPost post=new HttpPost("http://172.31.0.219:9200/test/_analyze?pretty");
//        HttpEntity entity=new StringEntity("fdafd fdasf","utf8");
//        post.setEntity(entity);
//        HttpResponse response=httpClient.execute(post);
//        byte[] bytes=new byte[2048];
//        response.getEntity().getContent().read(bytes);
//        System.out.println(new String(bytes));
    }
}


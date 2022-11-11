package com.example.consultacnpj;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.consultacnpj.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText rCnpj;
    TextView resultTV;
    Button btnConsulta;
    String nCnpj;
    String url_pesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rCnpj = findViewById(R.id.cnpj);
        resultTV = findViewById(R.id.result_tv);
        btnConsulta = findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.btnConsulta){
            nCnpj = rCnpj.getText().toString();
            url_pesquisa = "https://brasilapi.com.br/api/cnpj/v1/"+nCnpj;
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    public void getData() throws MalformedURLException{
        Uri uri = Uri.parse(url_pesquisa)
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }
    class DOTask extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data = NetworkUtils.makeHTTPRequest(url);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void parseJson(String data) throws JSONException{


            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            } catch (JSONException e){
                e.printStackTrace();
            }

            String r_social = jsonObject.getString("razao_social");
            String n_fantasia = jsonObject.getString("nome_fantasia");
            String situacao_cadastral = jsonObject.getString("descricao_situacao_cadastral");
            String c_f_desc = jsonObject.getString("cnae_fiscal_descricao");


            String resultado =
                    "Razão Social: \n\n"+r_social+
                    "\n\nNome Fantasia: \n\n"+n_fantasia+
                    "\n\nSituação Cadastral: \n\n"+situacao_cadastral+
                    "\n\nCnae Fiscal Descrição: \n\n"+c_f_desc;

            resultTV.setText(resultado);

        }

    }

}
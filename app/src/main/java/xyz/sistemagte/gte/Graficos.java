package xyz.sistemagte.gte;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xyz.sistemagte.gte.Auxiliares.GlobalUser;
import xyz.sistemagte.gte.Construtoras.EscolasConstr;

public class Graficos extends AppCompatActivity {

    private int idEmpresa, idUsuario;
    Spinner EscolaSpinner,spinerMeses;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    int idEscolaHolder;

    String HttpUrlSpinner = "https://sistemagte.xyz/json/ListarEscolasIdEmpresa.php";
    ArrayAdapter<String> EscolasListSpinner;
    ArrayList<EscolasConstr> EscolasListConst;

    RadioGroup radioGroup;
    String nomeEscolaHolder;
    boolean vAnual,vMensal;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);
        EscolaSpinner = findViewById(R.id.escolas);
        spinerMeses = findViewById(R.id.spinerMeses);
        requestQueue = Volley.newRequestQueue(this);
        EscolasListSpinner = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        EscolasListConst = new ArrayList<>();
        progressDialog = new ProgressDialog(Graficos.this);
        //botão
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle(getResources().getString(R.string.grafico));

        GlobalUser global = (GlobalUser) getApplication();
        idUsuario = global.getGlobalUserID();
        idEmpresa = global.getGlobalUserIdEmpresa();



        radioGroup = findViewById(R.id.radioGrup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.anual:
                        spinerMeses.setVisibility(View.GONE);
                        vAnual = true;
                        vMensal = false;
                        break;
                    case R.id.mensal:
                        spinerMeses.setVisibility(View.VISIBLE);
                        vMensal = true;
                        vAnual = false;
                        break;
                }
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrlSpinner,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        //Toast.makeText(cad_crianca.this, ServerResponse, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(ServerResponse);
                            JSONArray jsonArray = jsonObject.getJSONArray("nome");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String escola = jsonObject1.getString("nome");
                                EscolasListSpinner.add(escola);
                                EscolasConstr escolasConstr = new EscolasConstr(jsonObject1.getString("nome"), jsonObject1.getString("cep"), jsonObject1.getString("rua"), jsonObject1.getString("numero"), jsonObject1.getString("complemento"), jsonObject1.getString("estado"), jsonObject1.getString("cidade"), jsonObject1.getInt("idEscola"), jsonObject1.getInt("idEnderecoEscola"));
                                EscolasListConst.add(escolasConstr);
                            }
                            EscolaSpinner.setAdapter(EscolasListSpinner);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Graficos.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idEmpresa));

                return params;
            }

        };

        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, Painel_adm.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    //O botao padrao do android
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, Painel_adm.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    public void Enviar(View view) {

        if(!vAnual && !vMensal){
            Toast.makeText(this, "Selecione o tipo de gráfico.", Toast.LENGTH_SHORT).show();
        }else{
            if(vMensal && spinerMeses.getSelectedItemPosition() == 0){
                Toast.makeText(this, R.string.selecioneUmMes, Toast.LENGTH_SHORT).show();
                return;
            }
            int spinnerPos = EscolaSpinner.getSelectedItemPosition();
            EscolasConstr escolasConstr = EscolasListConst.get(spinnerPos);
            idEscolaHolder = escolasConstr.getIdEscola();//pegar a id da escola
            nomeEscolaHolder = escolasConstr.getNomeEscola();//pegar a id da escola
            if(vAnual){
                /**
                 * grafico anual
                 * */
                Intent Tela = new Intent(this,WebViewGraficos.class);
                Tela.putExtra("idEscola",String.valueOf(idEscolaHolder));
                Tela.putExtra("nomeEscola",String.valueOf(nomeEscolaHolder));
                Tela.putExtra("tipo","anual");
                startActivity(Tela);
            }else{

                Intent Tela = new Intent(this,WebViewGraficos.class);
                Tela.putExtra("tipo","mensal");
                Tela.putExtra("idEscola",String.valueOf(idEscolaHolder));
                Tela.putExtra("nomeEscola",String.valueOf(nomeEscolaHolder));
                Tela.putExtra("mes",spinerMeses.getSelectedItem().toString());
                startActivity(Tela);
            }
        }



    }
}

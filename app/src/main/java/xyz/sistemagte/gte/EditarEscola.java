package xyz.sistemagte.gte;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import xyz.sistemagte.gte.Auxiliares.GlobalUser;
import xyz.sistemagte.gte.Construtoras.EscolasConstr;

public class EditarEscola extends AppCompatActivity {


    private int idEmpresa,idUsuario,idCrianca;

    EditText Nome, CEP,Cidade, Rua, Numero, Complemento;
    Spinner Estado;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    String NomeHolder, CidadeHolder,CEPHolder,NumeroHolder,RuaHolder, ComplementoHolder, EstadoHolder;
    int idEscolaHolder,idEscola;
    String HttpUrl = "https://sistemagte.xyz/android/editar/editarCriancaResp.php";
    String HttpUrlSpinner = "https://sistemagte.xyz/json/ListarEscolasIdEmpresa.php";
    String JsonURLCrianca = "https://sistemagte.xyz/json/ListarDadosCriancaId.php";

    ArrayAdapter<String> EscolasListSpinner;
    ArrayList<EscolasConstr> EscolasListConst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_escola);

        Nome = findViewById(R.id.cad_nome);
        CEP = findViewById(R.id.cad_cep);
        Cidade = findViewById(R.id.cad_cidade);
        Rua = findViewById(R.id.cad_rua);
        Numero = findViewById(R.id.cad_num);
        Complemento = findViewById(R.id.cad_complemento);

        MaskEditTextChangedListener mascaraCEP = new MaskEditTextChangedListener("#####-###",CEP);
        CEP.addTextChangedListener(mascaraCEP);

        requestQueue = Volley.newRequestQueue(this);
        EscolasListSpinner = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        EscolasListConst = new ArrayList<>();
        progressDialog = new ProgressDialog(EditarEscola.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle(getResources().getString(R.string.editarCrianca));

        GlobalUser global =(GlobalUser)getApplication();
        idUsuario = global.getGlobalUserID();
        idEmpresa = global.getGlobalUserIdEmpresa();
        Intent i = getIntent();
        idEscola = Integer.parseInt(i.getStringExtra("idE"));
        PuxarDadosEscola();
    }

    //este é para o da navbar (seta)
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

    // Creating method to get value from EditText.
    public void GetValueFromEditText(){

        try {
            NomeHolder = Nome.getText().toString().trim();
            CEPHolder = CEP.getText().toString().trim();
            CidadeHolder = Cidade.getText().toString();
            RuaHolder = Rua.getText().toString();
            NumeroHolder = Numero.getText().toString();
            ComplementoHolder = Complemento.getText().toString();
            EstadoHolder = Estado.getSelectedItem().toString();

            switch (Estado.getSelectedItem().toString()) {//pega o nome do item ali em cima
                case "Acre":
                    EstadoHolder = "AC";
                    break;
                case "Alagoas":
                    EstadoHolder = "AL";
                    break;
                case "Amapá":
                    EstadoHolder = "AP";
                    break;
                case "Amazonas": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "AM";
                    break;
                case "Bahia": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "BA";
                    break;
                case "Ceará": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "CE";
                    break;
                case "Distrito Federal": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "DF";
                    break;
                case "Espírito Santo": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "ES";
                    break;
                case "Goiás": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "GO";
                    break;
                case "Maranhão": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "MA";
                    break;
                case "Mato Grosso": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "MT";
                    break;
                case "Mato Grosso do Sul": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "MS";
                    break;
                case "Minas Gerais": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "MG";
                    break;
                case "Pará": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "PA";
                    break;
                case "Paraiba": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "PB";
                    break;
                case "Paraná": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "PR";
                    break;
                case "Pernambuco": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "PE";
                    break;
                case "Piauí": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "PI";
                    break;
                case "Rio de Janeiro": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "RJ";
                    break;
                case "Rio Grande do Norte": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "RN";
                    break;
                case "Rio Grande do Sul": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "RS";
                    break;
                case "Rondônia": //este nome deve ser igual ao item ali de cima
                    EstadoHolder = "RO";
                    break;
                case "Roraima":
                    EstadoHolder = "RR";
                    break;
                case "Santa Catarina":
                    EstadoHolder = "SC";
                    break;
                case "São Paulo":
                    EstadoHolder = "SP";
                    break;
                case "Sergipe":
                    EstadoHolder = "SE";
                    break;
                case "Tocantinss":
                    EstadoHolder = "TO";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println(e.getMessage());
        }
    }

    public void Cadastrar(View view) {
        if(VerificarCampos()) {
            // Showing progress dialog at user registration time.
            progressDialog.setMessage(getResources().getString(R.string.loadingDados));
            progressDialog.show();

            // Calling method to get value from EditText.
            GetValueFromEditText();

            // Creating string request with post method.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String ServerResponse) {
                            progressDialog.dismiss();
                            Toast.makeText(EditarEscola.this, getResources().getString(R.string.informacoesSalvasSucesso), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressDialog.dismiss();
                            Toast.makeText(EditarEscola.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {

                    // Creating Map String Params.
                    Map<String, String> params = new HashMap<>();

                    // Adding All values to Params.
                    params.put("id", String.valueOf(idCrianca).trim());
                    params.put("nome", NomeHolder);
                    params.put("cep", CEPHolder);
                    params.put("cidade", CidadeHolder);
                    params.put("rua", RuaHolder);
                    params.put("numero", NumeroHolder);
                    params.put("complemento", ComplementoHolder);
                    params.put("estado", EstadoHolder);
                    params.put("idEscola", String.valueOf(idEscolaHolder));

                    return params;
                }

            };

            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

    }

    private boolean VerificarCampos(){
        if(Nome.getText().length() == 0 ||  CEP.getText().length() == 0 || Cidade.getText().length() == 0
                || Rua.getText().length() == 0 || Numero.getText().length() == 0 || Estado.getSelectedItemPosition() == 0){
            Toast.makeText(this, getResources().getString(R.string.verificarCampos), Toast.LENGTH_SHORT).show();
            return false;
        }else
        {
            return true;
        }
    }

    private void PuxarDadosEscola(){

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JsonURLCrianca,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        try{
                            JSONObject obj = new JSONObject(ServerResponse);

                            JSONArray funcArray = obj.getJSONArray("nome");
                            JSONObject jo = funcArray.getJSONObject(0);

                            Nome.setText(jo.getString("nome"));
                            String EstadoBD = jo.getString("estado");
                            CEP.setText(jo.getString("cep"));
                            Cidade.setText(jo.getString("cidade"));
                            Rua.setText(jo.getString("rua"));
                            Numero.setText(jo.getString("num"));
                            Complemento.setText(jo.getString("complemento"));

                            switch(EstadoBD){
                                case "AC"://acre
                                    Estado.setSelection(1);
                                    break;
                                case "AL"://alagoas
                                    Estado.setSelection(2);
                                    break;
                                case "AP"://amapa
                                    Estado.setSelection(3);
                                    break;
                                case"AM"://amazonas
                                    Estado.setSelection(4);
                                    break;
                                case"BA"://Bahia
                                    Estado.setSelection(5);
                                    break;
                                case "CE"://ceara
                                    Estado.setSelection(6);
                                    break;
                                case "DF"://distrito federal
                                    Estado.setSelection(7);
                                    break;
                                case "ES"://espirito santo
                                    Estado.setSelection(8);
                                    break;
                                case "GO"://goias
                                    Estado.setSelection(9);
                                    break;
                                case "MA"://maranhão
                                    Estado.setSelection(10);
                                    break;
                                case "MT"://mato grosso
                                    Estado.setSelection(11);
                                    break;
                                case "MS"://mato grosso do sul
                                    Estado.setSelection(12);
                                    break;
                                case "MG"://Minas gerais
                                    Estado.setSelection(13);
                                    break;
                                case "PA"://pará
                                    Estado.setSelection(14);
                                    break;
                                case "PB"://paraiba
                                    Estado.setSelection(15);
                                    break;
                                case "PR"://paraná 16
                                    Estado.setSelection(16);
                                    break;
                                case "PE"://pernambuco
                                    Estado.setSelection(17);
                                    break;
                                case "PI"://piaui
                                    Estado.setSelection(18);
                                    break;
                                case "RJ"://rio de janeiro
                                    Estado.setSelection(19);
                                    break;
                                case "RN"://rio grande do norte
                                    Estado.setSelection(20);
                                    break;
                                case "RS"://rio grande do sul
                                    Estado.setSelection(21);
                                    break;
                                case "RO"://rondonia
                                    Estado.setSelection(22);
                                    break;
                                case "RR"://roraima
                                    Estado.setSelection(23);
                                    break;
                                case "SC"://santa catarina
                                    Estado.setSelection(24);
                                    break;
                                case "SP"://são paulo
                                    Estado.setSelection(25);
                                    break;
                                case "SE"://sergipe
                                    Estado.setSelection(26);
                                    break;
                                case "TO"://tocantins
                                    Estado.setSelection(27);
                                    break;
                                default: Estado.setSelection(0);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(EditarEscola.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(EditarEscola.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",String.valueOf(idCrianca));

                return params;
            }

        };

        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
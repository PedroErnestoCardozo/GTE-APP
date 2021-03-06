package xyz.sistemagte.gte;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import xyz.sistemagte.gte.Auxiliares.GlobalUser;
import xyz.sistemagte.gte.Construtoras.EscolasConstr;
import xyz.sistemagte.gte.Construtoras.MonitoraConstr;
import xyz.sistemagte.gte.Construtoras.MotoristaConstr;
import xyz.sistemagte.gte.Construtoras.ResponsavelConstr;
import xyz.sistemagte.gte.Construtoras.VansConstr;

public class cad_van extends AppCompatActivity {

    EditText capacidade, modelo, placa,ano,marca;
    Spinner spinner, spinnerMonitora;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String HTTP_Cad = "https://sistemagte.xyz/android/cadastros/cadVan.php";
    String UrlSpinner = "https://sistemagte.xyz/json/adm/ListarMotoristaEmp.php";
    String UrlSpinner2 = "https://sistemagte.xyz/json/adm/ListarMonitoraEmp.php";

    Integer idEmpresa,idUsuario;

    ArrayAdapter<String> MotoristaListSpinner;
    ArrayAdapter<String> MonitoraListSpinner;
    ArrayList<MotoristaConstr> MotoristaConstrList;
    ArrayList<MonitoraConstr> MonitoraConstrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_van);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        GlobalUser global =(GlobalUser)getApplication();
        idUsuario = global.getGlobalUserID();
        idEmpresa = global.getGlobalUserIdEmpresa();


        capacidade = findViewById(R.id.cad_capacidade);
        modelo = findViewById(R.id.cad_modelo);
        placa = findViewById(R.id.cad_placa);
        ano = findViewById(R.id.cad_ano_fab);
        marca = findViewById(R.id.cad_marca);

        //Filtro para que todos os caracteres sejam maiusculos
        placa.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        spinnerMonitora = findViewById(R.id.monitoraSpinner);
        spinner = findViewById(R.id.morotistaSpinner);
        progressDialog = new ProgressDialog(cad_van.this);
        requestQueue = Volley.newRequestQueue(this);

        MotoristaConstrList = new ArrayList<>();
        MotoristaListSpinner = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);

        MonitoraConstrList = new ArrayList<>();
        MonitoraListSpinner = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);

        MaskEditTextChangedListener mascaraPlaca = new MaskEditTextChangedListener("###-####",placa);
        MaskEditTextChangedListener mascaraCapacidade = new MaskEditTextChangedListener("##",capacidade);
        MaskEditTextChangedListener mascaraAno = new MaskEditTextChangedListener("####",ano);

        placa.addTextChangedListener(mascaraPlaca);
        capacidade.addTextChangedListener(mascaraCapacidade);
        ano.addTextChangedListener(mascaraAno);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle(getResources().getString(R.string.cadastro_van));
        LoadMotoristas();
        LoadMonitoras();
    }

    //este é para o da navbar (seta)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, vans.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    //O botao padrao do android
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, vans.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    public void cadastrarVan(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HTTP_Cad,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        Toast.makeText(cad_van.this, R.string.cadastradoSucesso, Toast.LENGTH_SHORT).show();

                        Intent tela = new Intent(cad_van.this, vans.class);
                        startActivity(tela);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(cad_van.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                int spinnerPos2 = spinner.getSelectedItemPosition();
                MotoristaConstr motorista = MotoristaConstrList.get(spinnerPos2);

                int spinnerPos3 = spinner.getSelectedItemPosition();
                MotoristaConstr monitora = MotoristaConstrList.get(spinnerPos3);


                params.put("id", String.valueOf(motorista.getId_motorista()));
                params.put("idM", String.valueOf(monitora.getId_motorista()));
                params.put("capacidade", capacidade.getText().toString());
                params.put("modelo", modelo.getText().toString());
                params.put("placa", placa.getText().toString());
                params.put("AnoFabri", ano.getText().toString());
                params.put("marca", marca.getText().toString());

                return params;
            }

        };

        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void LoadMotoristas() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSpinner,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        try {
                            JSONObject jsonObject = new JSONObject(ServerResponse);
                            JSONArray jsonArray = jsonObject.getJSONArray("nome");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String motorista = jsonObject1.getString("nome") + " " + jsonObject1.getString("sobrenome");
                                MotoristaListSpinner.add(motorista);
                                MotoristaConstr motoristaConstr = new MotoristaConstr(jsonObject1.getString("nome"),jsonObject1.getString("sobrenome"),Integer.parseInt(jsonObject1.getString("id_usuario")));
                                MotoristaConstrList.add(motoristaConstr);
                            }
                            spinner.setAdapter(MotoristaListSpinner);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(cad_van.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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

    private void LoadMonitoras() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlSpinner2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        try {
                            JSONObject jsonObject = new JSONObject(ServerResponse);
                            JSONArray jsonArray = jsonObject.getJSONArray("nome");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String monitora = jsonObject1.getString("nome") + " " + jsonObject1.getString("sobrenome");
                                MonitoraListSpinner.add(monitora);
                                MonitoraConstr moni = new MonitoraConstr(jsonObject1.getString("nome"),jsonObject1.getString("sobrenome"),Integer.parseInt(jsonObject1.getString("id_usuario")));
                                MonitoraConstrList.add(moni);
                            }
                            spinnerMonitora.setAdapter(MonitoraListSpinner);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(cad_van.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<>();

                // Adding All values to Params.
                params.put("id", String.valueOf(idEmpresa));

                return params;
            }


        };

        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}

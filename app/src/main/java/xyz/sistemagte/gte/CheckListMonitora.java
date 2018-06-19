package xyz.sistemagte.gte;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.List;
import java.util.Map;

import xyz.sistemagte.gte.Auxiliares.GlobalUser;
import xyz.sistemagte.gte.Construtoras.CheckStatusConstr;
import xyz.sistemagte.gte.Construtoras.CriancaConst;
import xyz.sistemagte.gte.ListAdapters.ListViewCheck;
import xyz.sistemagte.gte.ListAdapters.ListViewCriancaAdm;

public class CheckListMonitora extends AppCompatActivity implements SearchView.OnQueryTextListener{


    private static String JSON_URL = "https://sistemagte.xyz/json/monitora/checklist.php";
    ListView listView;
    private int idEmpresa;
    private String idVan;
    List<CheckStatusConstr> checkList;
    List<CheckStatusConstr> listaQuery;
    AlertDialog alerta;
    SearchView searchView;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_monitora);
        GlobalUser global =(GlobalUser)getApplication();
        idEmpresa = global.getGlobalUserIdEmpresa();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent i = getIntent();
        idVan = i.getStringExtra("id");

        searchView = findViewById(R.id.barra_pesquisa);
        listView = findViewById(R.id.listView);
        checkList = new ArrayList<>();
        listaQuery = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle(getResources().getString(R.string.listaCriancas));     //Titulo para ser exibido na sua Action Bar em frente à seta

        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(CheckListMonitora.this);

        loadChecksList();

        listView.setTextFilterEnabled(true);
        setupSearchView();

    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);// definir se seria usado o icone ou o campo inteiro
        searchView.setOnQueryTextListener(this);//passagem do contexto para usar o searchview
        searchView.setSubmitButtonEnabled(false);//Defini se terá ou nao um o botao de submit
        searchView.setQueryHint(getString(R.string.pesquisarSearchPlaceholder));//Placeholder da searchbar
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, SelecionarVanMonitoraParaCheckList.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    //O botao padrao do android
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, SelecionarVanMonitoraParaCheckList.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    private void loadChecksList() {
        checkList.clear();
        // Showing progress dialog at user registration time.
        progressDialog.setMessage(getResources().getString(R.string.loadingRegistros));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println(idVan);
                        System.out.println(response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray funcArray = obj.getJSONArray("nome");

                            for (int i = 0; i < funcArray.length(); i++) {
                                JSONObject funcObject = funcArray.getJSONObject(i);
                                CheckStatusConstr checkStatusConstr = new CheckStatusConstr(Integer.parseInt(funcObject.getString("id_crianca")), funcObject.getString("nome"), funcObject.getString("sobrenome"),funcObject.getString("hora_entrada"), funcObject.getString("hora_escola"), funcObject.getString("hora_saida"),funcObject.getString("hora_casa"));

                                checkList.add(checkStatusConstr);
                                listaQuery.add(checkStatusConstr);
                            }
                            ListViewCheck adapter = new ListViewCheck(checkList, getApplicationContext());
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(CheckListMonitora.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(idVan));
                return params;
            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onQueryTextChange(String newText){
        listaQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            listaQuery.addAll(checkList);
        } else {
            String queryText = newText.toLowerCase();
            for(CheckStatusConstr obj : checkList){
                if(obj.getNomeCheck().toLowerCase().contains(queryText) ||
                        obj.getSobrenomeCheck().toLowerCase().contains(queryText)){
                    listaQuery.add(obj);
                }
            }
        }
        listView.setAdapter(new ListViewCheck(listaQuery, CheckListMonitora.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void MarcarCheck(View view) {
        listView.invalidateViews();
    }
}
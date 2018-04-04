package xyz.sistemagte.gte;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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
import java.util.List;

import xyz.sistemagte.gte.Auxiliares.GlobalUser;
import xyz.sistemagte.gte.Construtoras.FuncConst;
import xyz.sistemagte.gte.ListAdapters.ListViewFunc;


public class Funcionario extends AppCompatActivity {

    private static String JSON_URL = "https://sistemagte.xyz/json/adm/ListarFuncionarios.php?id=8";
    ListView listView;

    List<FuncConst> funcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionario);

        //TODO: Enviar a id da empresa como parametro por volley

        listView = findViewById(R.id.listView);
        funcList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle(getResources().getString(R.string.listaFunc));     //Titulo para ser exibido na sua Action Bar em frente à seta

        registerForContextMenu(listView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Cadastro de funcionários indisponível no momento", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadFuncList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getResources().getString(R.string.titleContextMenu));
        menu.add(0,v.getId(), 0, getResources().getString(R.string.editar));
        menu.add(0,v.getId(), 0, getResources().getString(R.string.excluir));
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle() == getResources().getString(R.string.editar)){
            Toast.makeText(this, "Editar", Toast.LENGTH_SHORT).show();
        }else if(item.getTitle() == getResources().getString(R.string.excluir)){
            Toast.makeText(this, "Deletar", Toast.LENGTH_SHORT).show();
        }
        return true;
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

    private void loadFuncList(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray funcArray = obj.getJSONArray("nome");

                            for (int i = 0; i < funcArray.length(); i++) {
                                JSONObject funcObject = funcArray.getJSONObject(i);
                                FuncConst funcConst = new FuncConst(funcObject.getString("nome"),funcObject.getString("sobrenome"),funcObject.getString("descricao"));

                                funcList.add(funcConst);
                            }

                            ListViewFunc adapter = new ListViewFunc(funcList, getApplicationContext());

                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Funcionario.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }

}

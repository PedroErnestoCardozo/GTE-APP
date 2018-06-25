package xyz.sistemagte.gte;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;


public class cad_motorista extends AppCompatActivity{

    EditText cep,cidade,rua,numero,complemento,cnh,validaCnh;
    Spinner sexo,categoria,Estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_motorista);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cep = findViewById(R.id.cad_cep);
        cidade = findViewById(R.id.cad_cidade);
        rua = findViewById(R.id.cad_rua);
        numero = findViewById(R.id.cad_num);
        complemento = findViewById(R.id.cad_complemento);
        cnh = findViewById(R.id.cad_cnh);
        validaCnh = findViewById(R.id.cad_ValidCnh);

        categoria = findViewById(R.id.cad_categoria);
        sexo = findViewById(R.id.cad_sexo);
        Estado = findViewById(R.id.cad_estado);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle(getResources().getString(R.string.cadastro_motorista));

        MaskEditTextChangedListener mascaraCPE = new MaskEditTextChangedListener("#####-###",cep);
        MaskEditTextChangedListener mascaraCNH = new MaskEditTextChangedListener("###########",cnh);
        MaskEditTextChangedListener mascaraValida  = new MaskEditTextChangedListener("##/##/####",validaCnh);

        cep.addTextChangedListener(mascaraCPE);
        cnh.addTextChangedListener(mascaraCNH);
        validaCnh.addTextChangedListener(mascaraValida);


        cep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String sendCep = cep.getText().toString();
                    sendCep = sendCep.replace(".", "");
                    sendCep = sendCep.replace("-", "");
                    String url = "https://viacep.com.br/ws/" + sendCep + "/json/unicode/";
                    StringRequest sr = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject objeto = new JSONObject(response);
                                String enderecoO = objeto.getString("logradouro"), cidadeO = objeto.getString("localidade");
                                rua.setText(enderecoO);
                                cidade.setText(cidadeO);
                                switch(objeto.getString("uf")){
                                    case "AC"://acre
                                        Estado.setSelection(1); // este numero é o numero do item,
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
                                        Estado.setSelection(6); // este numero é o numero do item,
                                        break;
                                    case "DF"://distrito federal
                                        Estado.setSelection(7); // este numero é o numero do item,
                                        break;
                                    case "ES"://espirito santo
                                        Estado.setSelection(8); // este numero é o numero do item,
                                        break;
                                    case "GO"://goias
                                        Estado.setSelection(9); // este numero é o numero do item,
                                        break;
                                    case "MA"://maranhão
                                        Estado.setSelection(10); // este numero é o numero do item,
                                        break;
                                    case "MT"://mato grosso
                                        Estado.setSelection(11); // este numero é o numero do item,
                                        break;
                                    case "MS"://mato grosso do sul
                                        Estado.setSelection(12); // este numero é o numero do item,
                                        break;
                                    case "MG"://Minas gerais
                                        Estado.setSelection(13); // este numero é o numero do item,
                                        break;
                                    case "PA"://pará
                                        Estado.setSelection(14); // este numero é o numero do item,
                                        break;
                                    case "PB"://paraiba
                                        Estado.setSelection(15); // este numero é o numero do item,
                                        break;
                                    case "PR"://paraná
                                        Estado.setSelection(16); // este numero é o numero do item,
                                        break;
                                    case "PE"://pernambuco
                                        Estado.setSelection(17); // este numero é o numero do item,
                                        break;
                                    case "PI"://piaui
                                        Estado.setSelection(18); // este numero é o numero do item,
                                        break;
                                    case "RJ"://rio de janeiro
                                        Estado.setSelection(19); // este numero é o numero do item,
                                        break;
                                    case "RN"://rio grande do norte
                                        Estado.setSelection(20); // este numero é o numero do item,
                                        break;
                                    case "RS"://rio grande do sul
                                        Estado.setSelection(21); // este numero é o numero do item,
                                        break;
                                    case "RO"://rondonia
                                        Estado.setSelection(22); // este numero é o numero do item,
                                        break;
                                    case "RR"://roraima
                                        Estado.setSelection(23); // este numero é o numero do item,
                                        break;
                                    case "SC"://santa catarina
                                        Estado.setSelection(24); // este numero é o numero do item,
                                        break;
                                    case "SP"://são paulo
                                        Estado.setSelection(25); // este numero é o numero do item,
                                        break;
                                    case "SE"://sergipe
                                        Estado.setSelection(26); // este numero é o numero do item,
                                        break;
                                    case "TO"://tocantins
                                        Estado.setSelection(1); // este numero é o numero do item,
                                        break;

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(cad_motorista.this, (getResources().getString(R.string.cepInvalido)), Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue rq = Volley.newRequestQueue(cad_motorista.this);
                    rq.add(sr);
                }
            }
        });


    }

    public void Cadastrar_motorista(View view) {
    }
}

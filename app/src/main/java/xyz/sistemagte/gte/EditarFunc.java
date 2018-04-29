package xyz.sistemagte.gte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class EditarFunc extends AppCompatActivity {

    EditText Nome,Sobrenome,Telefone,Email,DataNasc,Cpf,Rg,Cidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_func);


        Nome = findViewById(R.id.cad_nome);
        Sobrenome = findViewById(R.id.cad_sobrenome);
        Telefone = findViewById(R.id.cad_tel);
        Email = findViewById(R.id.cad_email);
        DataNasc = findViewById(R.id.cad_datanascimento);
        Cpf = findViewById(R.id.cad_cpf);
        Rg = findViewById(R.id.cad_rg);
        Cidade = findViewById(R.id.cad_cidade);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
            getSupportActionBar().setTitle(getResources().getString(R.string.editarFunc));     //Titulo para ser exibido na sua Action Bar em frente à seta


        MaskEditTextChangedListener mascaraCPF = new MaskEditTextChangedListener("###.###.###-##",Cpf);
        MaskEditTextChangedListener mascaraCelular = new MaskEditTextChangedListener("(##) #####-####",Telefone);
        MaskEditTextChangedListener mascaraData  = new MaskEditTextChangedListener("##/##/####",DataNasc);
        MaskEditTextChangedListener mascaraRG  = new MaskEditTextChangedListener("##.###.###-#",Rg);

        Cpf.addTextChangedListener(mascaraCPF);
        Telefone.addTextChangedListener(mascaraCelular);
        DataNasc.addTextChangedListener(mascaraData);
        Rg.addTextChangedListener(mascaraRG);
    }
}
package com.leandro.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leandro.retrofit.api.CEPService;
import com.leandro.retrofit.api.DataService;
import com.leandro.retrofit.model.CEP;
import com.leandro.retrofit.model.Foto;
import com.leandro.retrofit.model.Postagem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnAcoes;
    private TextView textoResultado;
    private Retrofit retrofit;
    private  List<Foto> listaFotos = new ArrayList<>();
    private  List<Postagem> listaPostagens = new ArrayList<>();
    private DataService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAcoes = findViewById(R.id.btnAcao);
        textoResultado = findViewById(R.id.txtResultado);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();

       /* retrofit = new Retrofit.Builder().baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create()).build();*/

        service = retrofit.create(DataService.class);

        btnAcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperarCep();
                //recuperarListaRetrofit();
               // salvarPostagemSemParamentros();
                //salvarPostagemComParametros();
               // atualizarPostagem();
                removerPostagem();

            }
        });
    }
    private void removerPostagem(){
        Call<Void>call = service.removerPostagem(2);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    textoResultado.setText("Status: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
    private  void atualizarPostagem(){
        //Configura objeto postagem
        //utilizando sem o path
        //Postagem postagem =  new Postagem("1234",null,"Corpo postagem");

        //usando metodo patch
        Postagem postagem = new Postagem();
        postagem.setBody("alterando com o patch.");

        //Call<Postagem> call = service.atualizarPostagem(2,postagem);
        Call<Postagem> call = service.atualizarPostagemPatch(2,postagem);

        call.enqueue(new Callback<Postagem>() {
            @Override
            public void onResponse(Call<Postagem> call, Response<Postagem> response) {
                if(response.isSuccessful()){
                    Postagem postagemResposta = response.body();
                    textoResultado.setText("Código: "+response.code()+
                            " id: "+postagemResposta.getId()+
                            " userId: "+postagemResposta.getUserId()+
                            " titulo: "+postagemResposta.getTitle()+
                            " body: "+postagemResposta.getBody());
                }
            }


            @Override
            public void onFailure(Call<Postagem> call, Throwable t) {

            }
        });

    }
    private void salvarPostagemComParametros(){
        service = retrofit.create(DataService.class);
        Call<Postagem>call = service.salvarPostagem("1234","Título postagem","Corpo postagem");

        call.enqueue(new Callback<Postagem>() {
            @Override
            public void onResponse(Call<Postagem> call, Response<Postagem> response) {
                if(response.isSuccessful()){
                    Postagem postagemResposta = response.body();
                    textoResultado.setText("Código: "+response.code()+" id: "+postagemResposta.getId()+
                            " titulo: "+postagemResposta.getTitle());
                }
            }

            @Override
            public void onFailure(Call<Postagem> call, Throwable t) {

            }
        });
    }
    private void salvarPostagemSemParamentros(){
        //configura objeto postagem
        Postagem postagem = new Postagem("1234","Título postagem","Corpo postagem");

        //recupera o serviço e salva postagem
        service = retrofit.create(DataService.class);
        Call<Postagem>call = service.salvarPostagem(postagem);

        call.enqueue(new Callback<Postagem>() {
            @Override
            public void onResponse(Call<Postagem> call, Response<Postagem> response) {
                if(response.isSuccessful()){
                    Postagem postagemResposta = response.body();
                    textoResultado.setText("Código: "+response.code()+" id: "+postagemResposta.getId()+
                            " titulo: "+postagemResposta.getTitle());
                }
            }

            @Override
            public void onFailure(Call<Postagem> call, Throwable t) {

            }
        });
    }

    private void recuperarListaRetrofit(){
        service = retrofit.create(DataService.class);
        Call<List<Foto>> call = service.recuperarFotos();

        call.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {
                if(response.isSuccessful()){
                    listaFotos = response.body();

                    for(int i=0; i<listaFotos.size(); i++){
                        Foto foto = listaFotos.get(i);
                        Log.d("resultado","resultado: "+foto.getId()+" / "+foto.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable t) {

            }
        });
    }

    private void recuperarCep(){
        CEPService cepService = retrofit.create(CEPService.class);
        Call<CEP> call = cepService.recuperarCEP("01310100");

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if(response.isSuccessful()){
                    CEP cep = response.body();
                    textoResultado.setText(cep.getLogradouro()+" / "+cep.getBairro());
                }
            }
            @Override
            public void onFailure(Call<CEP> call, Throwable t) {

            }
        });

    }
}

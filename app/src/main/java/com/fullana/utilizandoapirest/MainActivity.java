package com.fullana.utilizandoapirest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fullana.utilizandoapirest.poqueapi.PokeApiService;
import com.fullana.utilizandoapirest.poqueapi.Pokemon;
import com.fullana.utilizandoapirest.poqueapi.PokemonList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PokeApiService service = retrofit.create(PokeApiService.class);
        final int[] i = {0};
        CallbackP callbackP = new CallbackP() {
            @Override
            public void terminaBucle() {
                i[0] = -1;
            }
        };
        while(i[0]>=0){
                pokemons(service, i[0],callbackP);
                i[0] +=20;
        }

    }
    public void pokemons(PokeApiService pokeService, int offset, CallbackP callbackP) {
        Call<PokemonList> pokeCall = pokeService.getPokemonList(20,offset);
        pokeCall.enqueue(new Callback<PokemonList>() {
            @Override
            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
                // ANY CODE THAT DEPENDS ON THE RESULT OF THE SEARCH HAS TO GO HERE
                PokemonList foundPoke = response.body();
                // check if the body isn't null
                if (foundPoke != null) {
                    foundPoke.getResults().forEach((p) -> Log.println(Log.INFO,"Nombre: ",p.getName()));
                }
            }

            @Override
            public void onFailure(Call<PokemonList> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                callbackP.terminaBucle();
            }
        });
    }

    public void pokemonById(PokeApiService pokeService) {
        Call<Pokemon> pokeCall = pokeService.getPokemonById(Integer.toString((int) (Math.random() *807 + 1)));
        pokeCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
            // ANY CODE THAT DEPENDS ON THE RESULT OF THE SEARCH HAS TO GO HERE
                Pokemon foundPoke = response.body();
            // check if the body isn't null
                if (foundPoke != null) {
                    Log.d("POKEMON NAME", foundPoke.getName());
                    Log.d("POKEMON HEIGHT", foundPoke.getHeight());
                    Log.d("POKEMON WEIGHT", foundPoke.getWeight());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface CallbackP{
        void terminaBucle();
    }
}
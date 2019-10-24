package net.ivanvega.miaudiolibros;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class SelectorFragment extends Fragment {
    AppCompatActivity actividad;
    RecyclerView recyclerView;
    AdaptadorLibros adaptadorLibros;
    Vector<Libro> vectorLibros;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity){
            actividad = (AppCompatActivity) context;
            vectorLibros = Libro.ejemploLibros();
            adaptadorLibros = new
                    AdaptadorLibros
                    (context, vectorLibros);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_selector,
                container,
                false);

         recyclerView =
                 v.findViewById(R.id.recycler_view);
         recyclerView.setLayoutManager
                 (new GridLayoutManager(actividad,2));
         recyclerView.setAdapter(adaptadorLibros);

         adaptadorLibros.setOnclickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 /*Toast.makeText(actividad,
                         "Se prsiono el elemento: " +
                                 txt.getText().toString()
                         , Toast.LENGTH_LONG).show();*/
                 ((MainActivity) actividad).
                         mostrarDetalle(recyclerView.getChildAdapterPosition(view));
             }
         });

        adaptadorLibros.setOnItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final int id = recyclerView.getChildAdapterPosition(v);
                AlertDialog.Builder menu = new AlertDialog.Builder(actividad);
                CharSequence[] opciones = { "Compartir", "Borrar ", "Insertar" };
                menu.setItems(opciones,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int opcion) {
                                switch (opcion) {
                                    case 0: //Compartir
                                         Libro libro =
                                                 vectorLibros.elementAt(id);
                                         Intent i = new Intent(Intent.ACTION_SEND);
                                         i.setType("text/plain");
                                         i.putExtra(Intent.EXTRA_SUBJECT,
                                                 libro.titulo);
                                         i.putExtra(Intent.EXTRA_TEXT, libro.urlAudio);
                                         startActivity(Intent.createChooser(i,
                                                 "Compartir"));
                                         break;
                                     case 1: //Borrar
                                          vectorLibros.remove(id);
                                          adaptadorLibros.notifyDataSetChanged();
                                          break;

                                      case 2: //Insertar
                                          vectorLibros.add(vectorLibros.elementAt(id));
                                          adaptadorLibros.notifyDataSetChanged();
                                          break;
                                }
                            }
                });
                menu.create().show();

                return false;
            }
        }) ;

        return v;
    }
}

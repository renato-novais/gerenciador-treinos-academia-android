package br.edu.utfpr.renatonovais.gerenciadortreinosacademia.activitys;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.R;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.adapters.ExercicioAdapter;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.Exercicio;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.enums.Regiao;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.enums.Tipo;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.persistencia.ExercicioDatabase;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.utils.UtilsGUI;

public class ListagemExercicioActivity extends AppCompatActivity {

    private ListView listViewExercicio;
    private List<Exercicio> exercicios;

    private ExercicioAdapter listaAdapter;

    private int posicaoSelecionada = -1;

    private ActionMode actionMode;
    private View viewSelecionada;

    public static final String ARQUIVO = "br.edu.utfpr.renatonovais.gerenciadortreinosacademia.PREFERENCIAS";
    private boolean ordenacaoAscendente = true;

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.listagem_exercicio_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int idMenuItem = item.getItemId();

            if(idMenuItem == R.id.menuItemEditar) {
                alterarExercicio();
                mode.finish();
                return true;
            }
            if(idMenuItem == R.id.menuItemExcluir) {
                excluirExercicio(mode);
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelecionada != null) {
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            viewSelecionada = null;
            listViewExercicio.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_exercicio);
        setTitle(getString(R.string.listagem_de_exercicios));
        listViewExercicio = findViewById(R.id.listViewExercicio);
        listViewExercicio.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewExercicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                                   View view,
                                                   int position,
                                                   long id) {
                        posicaoSelecionada = position;
                        alterarExercicio();
                    }
                });

        listViewExercicio.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(actionMode != null) {
                    return false;
                }
                posicaoSelecionada = position;
                view.setBackgroundColor(Color.LTGRAY);
                viewSelecionada = view;
                listViewExercicio.setEnabled(false);
                actionMode = startSupportActionMode(mActionModeCallBack);
                return false;
            }
        });

        popularLista();
    }

    private void popularLista() {
        ExercicioDatabase database = ExercicioDatabase.getDatabase(this);

        if (ordenacaoAscendente){
            exercicios = database.getExercicioDao().queryAllAscending();
        }else{
            exercicios = database.getExercicioDao().queryAllDownward();
        }

        listaAdapter = new ExercicioAdapter(this,
                exercicios);

        listViewExercicio.setAdapter(listaAdapter);

    }

    private void alterarExercicio() {
        Exercicio exercicio = exercicios.get(posicaoSelecionada);

        FormExercicioActivity.alterarExercicio(this, launcherAlterarExercicio, exercicio);
    }

    private void excluirExercicio(final ActionMode mode) {
        Exercicio exercicio = exercicios.get(posicaoSelecionada);
        String mensagem = getString(R.string.deseja_realmente_apagar) + "\n" + "\"" + exercicio.getNome() + "\"";

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        ExercicioDatabase database = ExercicioDatabase.getDatabase(ListagemExercicioActivity.this);
                        int quantidadeAlterada = database.getExercicioDao().delete(exercicio);

                        if (quantidadeAlterada > 0){
                            exercicios.remove(posicaoSelecionada);
                            listaAdapter.notifyDataSetChanged();
                            mode.finish();
                        }else{
                            UtilsGUI.aviso(ListagemExercicioActivity.this, R.string.erro_ao_tentar_apagar);
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        UtilsGUI.confirmaAcao(this, mensagem, listener);
    }

    ActivityResultLauncher<Intent> launcherNovoExercicio = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK){

                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null){

                            long id = bundle.getLong(FormExercicioActivity.ID);
                            ExercicioDatabase database = ExercicioDatabase.getDatabase(ListagemExercicioActivity.this);

                            Exercicio exercicioInserido = database.getExercicioDao().queryForId(id);

                            exercicios.add(exercicioInserido);
                            Collections.sort(exercicios, Exercicio.comparator);

                            listaAdapter.notifyDataSetChanged();//se precisar tire
                        }
                    }
                }
            });
    ActivityResultLauncher<Intent> launcherAlterarExercicio = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),

            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK){

                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null){

                            long id = bundle.getLong(FormExercicioActivity.ID);
                            ExercicioDatabase database = ExercicioDatabase.getDatabase(ListagemExercicioActivity.this);

                            Exercicio exercicioEditado = database.getExercicioDao().queryForId(id);

                            exercicios.set(posicaoSelecionada, exercicioEditado);
                            Collections.sort(exercicios, Exercicio.comparator);

                            posicaoSelecionada = -1;

                            listaAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listagem_exercicio_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if (idMenuItem ==R.id.menuItemAdicionar) {
            FormExercicioActivity.novoExercicio(this, launcherNovoExercicio);
            return true;
        }
        if (idMenuItem ==R.id.menuItemSobre) {
            SobreActivity.sobre(this);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
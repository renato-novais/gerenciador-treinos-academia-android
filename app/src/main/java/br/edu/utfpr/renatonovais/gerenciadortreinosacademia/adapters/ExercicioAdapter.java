package br.edu.utfpr.renatonovais.gerenciadortreinosacademia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.R;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.Exercicio;

public class ExercicioAdapter extends BaseAdapter {
    private Context context;
    private List<Exercicio> exerciciosList;
    private String[] regioes;
    private String[] tipos;

    private static class ExercicioHolder {
        public TextView textViewValorNome;
        public TextView textViewValorTipo;
        public TextView textViewValorRegiao;
        public TextView textViewValorAnilhasHalteres;
    }

    public ExercicioAdapter(Context context, List<Exercicio> exercicios) {
        this.context = context;
        this.exerciciosList = exercicios;
        regioes = context.getResources().getStringArray(R.array.regioes_posicao);
        tipos = context.getResources().getStringArray(R.array.tipos_posicao);
    }

    @Override
    public int getCount() {
        return exerciciosList.size();
    }

    @Override
    public Object getItem(int position) {
        return exerciciosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ExercicioHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linha_lista_exercicios, parent, false);

            holder = new ExercicioHolder();

            holder.textViewValorNome = convertView.findViewById(R.id.textViewValorNome);
            holder.textViewValorTipo = convertView.findViewById(R.id.textViewValorTipo);
            holder.textViewValorRegiao = convertView.findViewById(R.id.textViewValorRegiao);
            holder.textViewValorAnilhasHalteres = convertView.findViewById(R.id.textViewValorAnilhasHalteres);

            convertView.setTag(holder);

        } else {
            holder = (ExercicioHolder) convertView.getTag();
        }
        Exercicio exercicio = exerciciosList.get(position);

        holder.textViewValorNome.setText(exercicio.getNome());

        switch (exercicio.getTipo()){
            case Musculacao:
                holder.textViewValorTipo.setText(R.string.musculacao);
                break;
            case Cardio:
                holder.textViewValorTipo.setText(R.string.cardio);
                break;
        }

        switch (exercicio.getRegiao()){
            case Peito:
                holder.textViewValorRegiao.setText(R.string.peito);
                break;
            case Costas:
                holder.textViewValorRegiao.setText(R.string.costas);
                break;
            case Ombros:
                holder.textViewValorRegiao.setText(R.string.ombros);
                break;
            case Bracos:
                holder.textViewValorRegiao.setText(R.string.bracos);
                break;
            case Pernas:
                holder.textViewValorRegiao.setText(R.string.pernas);
                break;
            case Abdomen:
                holder.textViewValorRegiao.setText(R.string.abdomen);
                break;
            case Aerobica:
                holder.textViewValorRegiao.setText(R.string.aerobica);
                break;
        }

        if (exercicio.isRequerAnilhaOuHalter()) {
            holder.textViewValorAnilhasHalteres.setText(R.string.requer);
        } else {
            holder.textViewValorAnilhasHalteres.setText(R.string.nao_requer);
        }

        return convertView;
    }

}

package co.minesweepers.mystockmyway.presenter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.minesweepers.mystockmyway.manager.StockManager;
import co.minesweepers.mystockmyway.model.Stock;
import co.minesweepers.mystockmyway.view.StockDetailView;

public class StockDetailsPresenter {
    private StockDetailView mView;
    private Stock mStock;

    public StockDetailsPresenter(Context context, String stockSymbol) {
        mStock = StockManager.getInstance(context).getStock(stockSymbol);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = mView.createView(inflater, container, savedInstanceState);
        mView.setStock(mStock);
        return view;
    }

    public void bind(final StockDetailView view) {
        mView = view;
    }
}

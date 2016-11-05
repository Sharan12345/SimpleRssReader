package com.simplerssreader;

import com.simplerssreader.main.PresenterImpl;
import com.simplerssreader.main.RssListContract;
import com.simplerssreader.model.SimpleItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link com.simplerssreader.main.PresenterImpl} logic.
 * <p>
 * Created by vgrec on 06.09.16.
 */
public class PresenterTest {

    private static final String LINK = "http://google.com";
    private RssListContract.View mockView;
    private Observable<List<SimpleItem>> mockObservable;

    private RssListContract.Presenter presenter;
    private List<SimpleItem> rssItems = new ArrayList<>();
    private SimpleItem item;

    @Before
    public void setUp() {
        Utils.setupRxJava();

        mockView = mock(RssListContract.View.class);
        presenter = new PresenterImpl(mockView);

        // setup test data
        item = new SimpleItem("Mock Item", LINK);
        rssItems.add(item);
    }

    @Test
    public void shouldOpenWebView() {
        presenter.viewArticleDetail(item);
        verify(mockView).openInWebView(LINK);
    }


    @Test
    public void shouldLoadRssItem() {
        mockObservable = Observable.just(rssItems);
        presenter.loadItems();
        verify(mockView, times(1)).showLoadingIndicator(true);
        verify(mockView, times(1)).showLoadingIndicator(false);
        verify(mockView).showItems(anyListOf(SimpleItem.class));
    }

    @Test
    public void shouldShowError() {
        mockObservable = Observable.error(new IOException());
        presenter.loadItems();
        verify(mockView, never()).showItems(anyListOf(SimpleItem.class));
        verify(mockView).showLoadingIndicator(false);
        verify(mockView).showError();
    }

    @After
    public void tearDown() {
        Utils.resetRxJava();
    }

}

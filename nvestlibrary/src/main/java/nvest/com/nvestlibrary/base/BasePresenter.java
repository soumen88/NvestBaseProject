package nvest.com.nvestlibrary.base;

import nvest.com.nvestlibrary.nvestWebClient.NvestWebApiClient;
import nvest.com.nvestlibrary.nvestWebInterface.NvestWebApiInterface;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = BasePresenter.class.getSimpleName();

    private V mMvpView;

    public BasePresenter() {
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }

    @Override
    public NvestWebApiInterface getApiInterface() {
        return NvestWebApiClient.getClient().create(NvestWebApiInterface.class);
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}

package com.example.staffmanagement.MVVM.Model.Repository.Base;

public abstract class NetworkBoundResource<ResultType, RequestType> implements ApiResponse<RequestType> {

    protected abstract ResultType loadFromDb();

    protected abstract boolean shouldFetchData(ResultType data);

    protected abstract void createCall(ApiResponse apiResponse);

    protected abstract void saveCallResult(RequestType data);

    protected abstract void onFetchFail();

    protected abstract void onFetchSuccess(ResultType data);

    private volatile Resource<RequestType> mResource;

    private ResultType mResultType;

    public ResultType run() {
        Thread th1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ResultType resultType = loadFromDb();
                if (shouldFetchData(resultType)) {
                    createCall(NetworkBoundResource.this);
                } else {
                    onFetchSuccess(resultType);
                    mResultType = resultType;
                }
            }
        });
        th1.start();

        Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mResultType == null && mResource == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        th2.start();
        try {
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mResultType;
    }

    @Override
    public void onSuccess(final Resource<RequestType> success) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mResource = success;
                saveCallResult(success.getData());
                ResultType resultType = loadFromDb();
                onFetchSuccess(resultType);
                mResultType = resultType;
            }
        }).start();
    }

    @Override
    public void onError(Resource<RequestType> error) {
        mResource = error;
    }

    @Override
    public void onLoading(Resource<RequestType> loading) {

    }
}

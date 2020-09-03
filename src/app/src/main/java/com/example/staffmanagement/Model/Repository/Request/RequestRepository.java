package com.example.staffmanagement.Model.Repository.Request;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.Rule;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Request.RequestService;
import com.example.staffmanagement.Model.FirebaseDb.User.UserService;
import com.example.staffmanagement.Model.Repository.NotificationRepository;
import com.example.staffmanagement.Model.Ultils.ConstString;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Notification.Sender.DataStaffRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RequestRepository {
    private RequestService service;
    private MutableLiveData<List<Request>> mLiveData;

    public RequestRepository() {
        service = new RequestService();
        this.mLiveData = new MutableLiveData<>();
    }

    public void getLimitListRequestForStaffLD(int idUser, int offset, int numRow, StaffRequestFilter criteria) {
        service.getListByIdUser(idUser, new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                new Thread(() -> {
                    String searchString = criteria.getSearchString();
                    List<Request> list = success.getData();
                    list = list.stream()
                            .filter(request -> request.getTitle().toLowerCase().contains(searchString.toLowerCase()))
                            .collect(Collectors.toList());

                    if (criteria.getStateList().size() > 0) {
                        List<Request> temp = new ArrayList<>();
                        temp.addAll(list);
                        list.clear();
                        for (int i = 0; i < criteria.getStateList().size(); i++) {
                            if (criteria.getStateList().get(i).toString().equals(StaffRequestFilter.STATE.Waiting.toString()))
                                list.addAll(temp.stream().filter(request -> request.getStateRequest().getId() == 1).collect(Collectors.toList()));
                            else if (criteria.getStateList().get(i).toString().equals(StaffRequestFilter.STATE.Accept.toString()))
                                list.addAll(temp.stream().filter(request -> request.getStateRequest().getId() == 2).collect(Collectors.toList()));
                            else if (criteria.getStateList().get(i).toString().equals(StaffRequestFilter.STATE.Decline.toString()))
                                list.addAll(temp.stream().filter(request -> request.getStateRequest().getId() == 3).collect(Collectors.toList()));
                        }
                        temp.clear();
                    }

                    if (criteria.getFromDateTime() != 0 && criteria.getToDateTime() != 0) {
                        list = list.stream()
                                .filter(request -> request.getDateTime() > criteria.getFromDateTime() && request.getDateTime() < criteria.getToDateTime())
                                .collect(Collectors.toList());
                    }

                    if (!criteria.getSortName().equals(StaffRequestFilter.SORT.None)) {

                        switch (criteria.getSortName()) {
                            case DateTime:
                                if (criteria.getSortType() == StaffRequestFilter.SORT_TYPE.ASC)
                                    list.sort((request, t1) -> request.getDateTime() < t1.getDateTime() ? -1 : 1);
                                else
                                    list.sort((request, t1) -> request.getDateTime() > t1.getDateTime() ? -1 : 1);
                                break;
                            case Title:
                                if (criteria.getSortType() == StaffRequestFilter.SORT_TYPE.ASC)
                                    list.sort((request, t1) -> request.getTitle().compareTo(t1.getTitle()) > 0 ? -1 : 1);
                                else
                                    list.sort((request, t1) -> request.getTitle().compareTo(t1.getTitle()) < 0 ? -1 : 1);
                                break;
                        }

                    } else
                        list.sort((request, t1) -> request.getDateTime() > t1.getDateTime() ? -1 : 1);

                    list = list.stream().skip(offset).limit(numRow).collect(Collectors.toList());
                    mLiveData.postValue(list);
                }).start();
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {
                mLiveData.postValue(new ArrayList<>());
            }
        });

    }

    public MutableLiveData<List<Request>> getLiveData() {
        return mLiveData;
    }

    public void getLimitListRequestForUser(int idUser, int offset, int numRow, AdminRequestFilter criteria) {

        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                new Thread(() -> {
                    String searchString = criteria.getSearchString();
                    List<Request> list = success.getData();
                    if (idUser != 0)
                        list = list.stream().filter(request -> request.getIdUser() == idUser).collect(Collectors.toList());

                    list = list.stream()
                            .filter(request -> request.getNameOfUser().toLowerCase().contains(searchString.toLowerCase()))
                            .collect(Collectors.toList());

                    if (criteria.getStateList().size() > 0) {
                        List<Request> temp = new ArrayList<>();
                        temp.addAll(list);
                        list.clear();
                        for (int i = 0; i < criteria.getStateList().size(); i++) {
                            if (criteria.getStateList().get(i).toString().equals(StaffRequestFilter.STATE.Waiting.toString()))
                                list.addAll(temp.stream().filter(request -> request.getStateRequest().getId() == 1).collect(Collectors.toList()));
                            else if (criteria.getStateList().get(i).toString().equals(StaffRequestFilter.STATE.Accept.toString()))
                                list.addAll(temp.stream().filter(request -> request.getStateRequest().getId() == 2).collect(Collectors.toList()));
                            else if (criteria.getStateList().get(i).toString().equals(StaffRequestFilter.STATE.Decline.toString()))
                                list.addAll(temp.stream().filter(request -> request.getStateRequest().getId() == 3).collect(Collectors.toList()));
                        }
                        temp.clear();
                    }

                    if (criteria.getFromDateTime() != 0 && criteria.getToDateTime() != 0) {
                        list = list.stream()
                                .filter(request -> request.getDateTime() > criteria.getFromDateTime() && request.getDateTime() < criteria.getToDateTime())
                                .collect(Collectors.toList());
                    }

                    if (!criteria.getSortName().equals(AdminRequestFilter.SORT.None)) {

                        switch (criteria.getSortName()) {
                            case DateTime:
                                if (criteria.getSortType() == AdminRequestFilter.SORT_TYPE.ASC)
                                    list.sort((request, t1) -> request.getDateTime() < t1.getDateTime() ? -1 : 1);
                                else
                                    list.sort((request, t1) -> request.getDateTime() > t1.getDateTime() ? -1 : 1);
                                break;
                            case Title:
                                if (criteria.getSortType() == AdminRequestFilter.SORT_TYPE.ASC)
                                    list.sort((request, t1) -> request.getNameOfUser().compareTo(t1.getNameOfUser()) > 0 ? -1 : 1);
                                else
                                    list.sort((request, t1) -> request.getNameOfUser().compareTo(t1.getNameOfUser()) < 0 ? -1 : 1);
                                break;
                        }

                    } else
                        list.sort((request, t1) -> request.getDateTime() > t1.getDateTime() ? -1 : 1);

                    list = list.stream().skip(offset).limit(numRow).collect(Collectors.toList());
                    mLiveData.postValue(list);

                }).start();
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {
                mLiveData.postValue(new ArrayList<>());
                mLiveData.postValue(new ArrayList<>());
            }
        });
    }

    public void restoreRequest(Request request) {
        service.update(request);
    }

    public void insert(Request request, final int idUser, final int offset, final StaffRequestFilter criteria) {
        service.put(request, new ApiResponse<Request>() {
            @Override
            public void onSuccess(Resource<Request> success) {
                //getLimitListRequestForStaffLD(idUser, offset, 1, criteria);
                sendMessageToAdmin(success.getData().getId());
            }

            @Override
            public void onLoading(Resource<Request> loading) {

            }

            @Override
            public void onError(Resource<Request> error) {

            }
        });
    }

    private void sendMessageToAdmin(int idRequest) {
        DataStaffRequest data = new DataStaffRequest("New request"
                , "You have new request from " + UserSingleTon.getInstance().getUser().getFullName()
                , "request"
                , idRequest);
        sendNotification(data);
    }

    public void sendNotification(DataStaffRequest data){
        new NotificationRepository().sendNotificationForAllAdmin(data, new CallBackFunc<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {

            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void updateRequest(Request request) {
        service.update(request);
    }

    public void deleteRequest(Request request) {
        service.delete(request.getIdUser(), request.getId());
    }

    public void countRequestWaiting(CallBackFunc<Integer> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                new Thread(() -> {
                    long count = success.getData()
                            .stream()
                            .filter(stateRequest -> stateRequest.getStateRequest().getId() == 1)
                            .count();
                    callBackFunc.onSuccess((int) count);
                }).start();
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void countRequestResponse(CallBackFunc<Integer> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                new Thread(() -> {
                    long count = success.getData()
                            .stream()
                            .filter(stateRequest -> stateRequest.getStateRequest().getId() == 2 || stateRequest.getStateRequest().getId() == 3)
                            .count();
                    callBackFunc.onSuccess((int) count);
                }).start();
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void countRecentRequest(CallBackFunc<Integer> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                int d = 0;
                for (int i = 0; i < success.getData().size(); i++) {
                    if ((new Date().getTime() - success.getData().get(i).getDateTime()) <= ConstString.LIMIT_TIME_RECENT_REQUEST) {
                        d++;
                    }
                }
                callBackFunc.onSuccess(d);
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void countAllRequest(CallBackFunc<Integer> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                new Thread(() -> {
                    int count = success.getData().size();
                    callBackFunc.onSuccess(count);
                }).start();
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void countMostUserSendingRequest(CallBackFunc<String> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success1) {
                new UserService().getAll(new ApiResponse<List<User>>() {
                    @Override
                    public void onSuccess(Resource<List<User>> success) {
                        new Thread(() -> {
                            List<Integer> quantityRequest = new ArrayList<>();
                            for (int i = 0; i < success.getData().size(); i++) {
                                final int ii = i;
                                long count = success1.getData()
                                        .stream()
                                        .filter(request -> request.getIdUser() == success.getData().get(ii).getId())
                                        .count();
                                quantityRequest.add((int) count);
                            }
                            int max = 0;
                            String name = "";
                            for (int i = 0; i < quantityRequest.size(); i++) {
                                if (quantityRequest.get(i) > max) {
                                    max = quantityRequest.get(i);
                                    name = success.getData().get(i).getFullName();
                                }
                            }
                            callBackFunc.onSuccess(name + " - request : " + max);
                        }).start();
                    }

                    @Override
                    public void onLoading(Resource<List<User>> loading) {

                    }

                    @Override
                    public void onError(Resource<List<User>> error) {

                    }
                });
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void countLeastUserSendingRequest(CallBackFunc<String> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success1) {
                new UserService().getAll(new ApiResponse<List<User>>() {
                    @Override
                    public void onSuccess(Resource<List<User>> success) {
                        new Thread(() -> {
                            List<Integer> quantityRequest = new ArrayList<>();
                            for (int i = 0; i < success.getData().size(); i++) {
                                if (success.getData().get(i).getRole().getId() == 2) {
                                    final int ii = i;
                                    long count = success1.getData()
                                            .stream()
                                            .filter(request -> request.getIdUser() == success.getData().get(ii).getId())
                                            .count();
                                    quantityRequest.add((int) count);
                                }
                            }
                            if (quantityRequest.size() == 0) {
                                callBackFunc.onSuccess("No data");
                                return;
                            }

                            int min = quantityRequest.get(0);
                            String name = "No staff";
                            User u = success.getData().stream()
                                    .filter(user -> user.getRole().getId() == 2)
                                    .findFirst()
                                    .orElse(null);
                            if (u != null)
                                name = u.getFullName();
                            for (int i = 1; i < quantityRequest.size(); i++) {
                                if (quantityRequest.get(i) < min) {
                                    min = quantityRequest.get(i);
                                    name = success.getData().get(i).getFullName();
                                }
                            }
                            callBackFunc.onSuccess(name + " - request : " + min);
                        }).start();
                    }

                    @Override
                    public void onLoading(Resource<List<User>> loading) {

                    }

                    @Override
                    public void onError(Resource<List<User>> error) {

                    }
                });
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void TotalRequestForUser(int idUser, CallBackFunc<Integer> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {

            @Override
            public void onSuccess(Resource<List<Request>> success) {
                int requestTotal = (int) success.getData()
                        .stream()
                        .filter(request -> request.getIdUser() == idUser)
                        .count();
                callBackFunc.onSuccess(requestTotal);
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void PieChart(int idUser, CallBackFunc<List<Float>> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                List<Float> floats = new ArrayList<>();
                float waiting = success.getData()
                        .stream().filter(request -> request.getIdUser() == idUser &&
                                request.getStateRequest().getId() == 1)
                        .count();
                float accept = success.getData()
                        .stream().filter(request -> request.getIdUser() == idUser &&
                                request.getStateRequest().getId() == 2)
                        .count();
                float decline = success.getData()
                        .stream().filter(request -> request.getIdUser() == idUser &&
                                request.getStateRequest().getId() == 3)
                        .count();
                floats.add(waiting);
                floats.add(accept);
                floats.add(decline);
                callBackFunc.onSuccess(floats);
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void StateRequestForUser(int idUser, int idStateRequest, CallBackFunc<Integer> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {

            @Override
            public void onSuccess(Resource<List<Request>> success) {
                int stateRequestCount = (int) success.getData()
                        .stream().filter(request -> request.getIdUser() == idUser &&
                                request.getStateRequest().getId() == idStateRequest)
                        .count();
                callBackFunc.onSuccess(stateRequestCount);
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void getRule(CallBackFunc<Rule> callBackFunc) {
        service.getRule(new ApiResponse<Rule>() {
            @Override
            public void onSuccess(Resource<Rule> success) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        callBackFunc.onSuccess(success.getData());
                    }
                }).start();
            }

            @Override
            public void onLoading(Resource<Rule> loading) {

            }

            @Override
            public void onError(Resource<Rule> error) {
                callBackFunc.onError(error.getMessage());
            }
        });
    }

    public void updateRule(Rule rule, CallBackFunc<Rule> callBackFunc) {
        service.updateRule(rule, new ApiResponse<Rule>() {
            @Override
            public void onSuccess(Resource<Rule> success) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        callBackFunc.onSuccess(success.getData());
                    }
                }).start();
            }

            @Override
            public void onLoading(Resource<Rule> loading) {

            }

            @Override
            public void onError(Resource<Rule> error) {
                callBackFunc.onError(error.getMessage());
            }
        });
    }


    public void checkRuleForAddRequest(int idUser, CallBackFunc<Boolean> callBackFunc) {
        service.getRule(new ApiResponse<Rule>() {
            @Override
            public void onSuccess(Resource<Rule> successRule) {
                service.getListByIdUser(idUser, new ApiResponse<List<Request>>() {
                    @Override
                    public void onSuccess(Resource<List<Request>> success) {
                        // milli second
                        long rangeDay = 3600 * 24 * 1000 * successRule.getData().getPeriod();
                        rangeDay = Math.abs(rangeDay);
                        long nowDay = new Date().getTime();
                        long minLimit = nowDay - rangeDay;
                        int d = 0;

                        List<Request> list = success.getData()
                                .stream()
                                .sorted((request, t1) -> request.getDateTime() > t1.getDateTime() ? -1 : 1)
                                .collect(Collectors.toList());

                        for (Request request : list) {
                            if (request.getDateTime() >= minLimit && request.getDateTime() <= nowDay) {
                                d++;
                            }
                            if (d >= successRule.getData().getMaxNumberRequestOfRule()) {
                                callBackFunc.onSuccess(false);
                                break;
                            }
                        }
                        if (d < successRule.getData().getMaxNumberRequestOfRule())
                            callBackFunc.onSuccess(true);
                    }

                    @Override
                    public void onLoading(Resource<List<Request>> loading) {

                    }

                    @Override
                    public void onError(Resource<List<Request>> error) {

                    }
                });
            }

            @Override
            public void onLoading(Resource<Rule> loading) {

            }

            @Override
            public void onError(Resource<Rule> error) {
                callBackFunc.onError(error.getMessage());
            }
        });
    }

    public void getRequestById(int id, CallBackFunc<Request> callBackFunc) {
        service.getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                new Thread(() -> {
                    int flag = 0;
                    for (int i = 0; i < success.getData().size(); i++) {
                        if(id == success.getData().get(i).getId()){
                            flag = 1;
                            callBackFunc.onSuccess(success.getData().get(i));
                            break;
                        }
                    }

                    if(flag == 0){
                        callBackFunc.onSuccess(null);
                    }
                }).start();
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

}




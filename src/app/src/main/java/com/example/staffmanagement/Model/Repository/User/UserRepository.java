package com.example.staffmanagement.Model.Repository.User;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.NetworkBoundResource;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Request.RequestService;
import com.example.staffmanagement.Model.FirebaseDb.User.UserService;
import com.example.staffmanagement.Model.Repository.AppDatabase;
import com.example.staffmanagement.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.Model.Repository.Role.RoleRepository;
import com.example.staffmanagement.Model.Repository.UserState.UserStateRepository;
import com.example.staffmanagement.Model.Ultils.UserQuery;
import com.example.staffmanagement.View.Ultils.Constant;
import com.example.staffmanagement.ViewModel.CallBackFunc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class UserRepository {
    private UserService service;
    private MutableLiveData<List<User>> mLiveDataUser;
    private MutableLiveData<List<Integer>> mLiveDataQuantities;
    private MutableLiveData<List<Role>> mLiveDataRole;
    private MutableLiveData<List<UserState>> mLiveDataUserState;
    private MutableLiveData<List<User>> mLiveDataUserCheck;
    private MutableLiveData<List<String>> listFullName;
    private MutableLiveData<User> mUserLD;

    public UserRepository() {
        service = new UserService();
        mLiveDataUser = new MutableLiveData<>();
        mLiveDataQuantities = new MutableLiveData<>();
        mLiveDataRole = new MutableLiveData<>();
        mLiveDataUserState = new MutableLiveData<>();
        mLiveDataUserCheck = new MutableLiveData<>();
        listFullName = new MutableLiveData<>();
        mUserLD = new MutableLiveData<>();
    }


    public MutableLiveData<List<User>> getLiveData() {
        return mLiveDataUser;
    }

    public MutableLiveData<List<Integer>> getLiveDataQuantities() {
        return mLiveDataQuantities;
    }

    public MutableLiveData<List<Role>> getLiveDataRole() {
        return mLiveDataRole;
    }

    public MutableLiveData<List<UserState>> getLiveDataUserState() {
        return mLiveDataUserState;
    }

    public MutableLiveData<List<User>> getLiveDataUserCheck() {
        return mLiveDataUserCheck;
    }

    public MutableLiveData<User> getUserLD() {
        return mUserLD;
    }

    public void getLimitListUser(int idUser, int offset, int numRow, Map<String, Object> criteria) {
        service.getAll(new ApiResponse<List<User>>() {
            @Override
            public void onSuccess(Resource<List<User>> success) {
                String searchString = (String) criteria.get(Constant.SEARCH_NAME_IN_ADMIN);
                List<User> userList = success.getData();
                userList = userList.stream()
                        .filter(user -> user.getIdRole() == 2 &&
                                user.getUserName().toLowerCase().contains(searchString.toLowerCase()))
                        .skip(offset)
                        .limit(numRow)
                        .collect(Collectors.toList());

                List<User> finalUserList = userList;
                new Thread(() -> new RequestService().getAll(new ApiResponse<List<Request>>() {
                    @Override
                    public void onSuccess(Resource<List<Request>> successReq) {
                        List<Integer> quantities = new ArrayList<>();
                        for (User user : finalUserList) {
                            long count = successReq.getData()
                                    .stream()
                                    .filter(request -> request.getIdUser() == user.getId() && request.getIdState() == 1)
                                    .count();
                            quantities.add((int) count);
                        }

                        mLiveDataQuantities.postValue(quantities);
                        mLiveDataUser.postValue(finalUserList);
                    }

                    @Override
                    public void onLoading(Resource<List<Request>> loading) {

                    }

                    @Override
                    public void onError(Resource<List<Request>> error) {

                    }
                })).start();

            }

            @Override
            public void onLoading(Resource<List<User>> loading) {

            }

            @Override
            public void onError(Resource<List<User>> error) {

            }
        });
    }

    public void getAllRoleAndUserState() {
        new RoleRepository().getAll(new CallBackFunc<List<Role>>() {
            @Override
            public void success(List<Role> dataRoles) {
                new UserStateRepository().getAll(new CallBackFunc<List<UserState>>() {
                    @Override
                    public void success(List<UserState> data) {
                        mLiveDataUserState.postValue(data);
                        mLiveDataRole.postValue(dataRoles);
                    }

                    @Override
                    public void error(String message) {

                    }
                });
            }

            @Override
            public void error(String message) {

            }
        });
    }

    public void populateData() {
        service.populateData();
    }

    public void updateUser(User user) {
        service.update(user, new ApiResponse<User>() {
            @Override
            public void onSuccess(Resource<User> success) {

            }

            @Override
            public void onLoading(Resource<User> loading) {

            }

            @Override
            public void onError(Resource<User> error) {

            }
        });
    }

    public void changeAvatarUser(User user, Bitmap bitmap, CallBackFunc<User> callBackFunc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.changeAvatar(user, bitmap, new ApiResponse<User>() {
                    @Override
                    public void onSuccess(Resource<User> success) {
                        callBackFunc.success(user);
                    }

                    @Override
                    public void onLoading(Resource<User> loading) {

                    }

                    @Override
                    public void onError(Resource<User> error) {

                    }
                });
            }
        }).start();
    }

    public void getUserForLogin(final int idUser) {
        service.getById(idUser, new ApiResponse<User>() {
            @Override
            public void onSuccess(Resource<User> success) {
                mUserLD.postValue(success.getData());
            }

            @Override
            public void onLoading(Resource<User> loading) {

            }

            @Override
            public void onError(Resource<User> error) {
                mUserLD.postValue(null);
            }
        });
    }

    public void getByLoginInformation(String userName, String password) {

        service.getAll(new ApiResponse<List<User>>() {
            @Override
            public void onSuccess(Resource<List<User>> success) {
                new Thread(() -> {
                    int f = 0;
                    for (User u : success.getData()) {
                        if (u.getUserName().equals(userName) && u.getPassword().equals(password)) {
                            mUserLD.postValue(u);
                            f = 1;
                            return;
                        }
                    }
                    if (f == 0)
                        mUserLD.postValue(null);
                }).start();

            }

            @Override
            public void onLoading(Resource<List<User>> loading) {

            }

            @Override
            public void onError(Resource<List<User>> error) {
                mUserLD.postValue(null);
            }
        });
    }

    public void insert(User user, final int idUser, final int offset, final Map<String, Object> mCriteria) {
        service.put(user, new ApiResponse<User>() {
            @Override
            public void onSuccess(Resource<User> success) {
                getLimitListUser(idUser, offset, 1, mCriteria);
            }

            @Override
            public void onLoading(Resource<User> loading) {

            }

            @Override
            public void onError(Resource<User> error) {

            }
        });
    }

    public void getCountStaff(CallBackFunc<Integer> callBackFunc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.getAll(new ApiResponse<List<User>>() {
                    @Override
                    public void onSuccess(Resource<List<User>> success) {
                        long count = success.getData()
                                .stream()
                                .filter(user -> user.getIdRole() == 2)
                                .count();
                        callBackFunc.success((int) count);
                    }

                    @Override
                    public void onLoading(Resource<List<User>> loading) {

                    }

                    @Override
                    public void onError(Resource<List<User>> error) {

                    }
                });
            }
        }).start();
    }

    public void getAllStaff(CallBackFunc<List<User>> callBackFunc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.getAll(new ApiResponse<List<User>>() {
                    @Override
                    public void onSuccess(Resource<List<User>> success) {
                        List<User> list = success.getData()
                                .stream()
                                .filter(user -> user.getIdRole() == 2)
                                .collect(Collectors.toList());
                        callBackFunc.success(list);
                    }

                    @Override
                    public void onLoading(Resource<List<User>> loading) {

                    }

                    @Override
                    public void onError(Resource<List<User>> error) {

                    }
                });
            }
        }).start();
    }

    public void changeIdUserState(int idUser, int idUserState) {
        service.getById(idUser, new ApiResponse<User>() {
            @Override
            public void onSuccess(Resource<User> success) {
                User user = success.getData();
                user.setIdUserState(idUserState);
                service.update(user, new ApiResponse<User>() {
                    @Override
                    public void onSuccess(Resource<User> success) {

                    }

                    @Override
                    public void onLoading(Resource<User> loading) {

                    }

                    @Override
                    public void onError(Resource<User> error) {

                    }
                });
            }

            @Override
            public void onLoading(Resource<User> loading) {

            }

            @Override
            public void onError(Resource<User> error) {

            }
        });
    }

    public void resetPassword(int idUser) {
        service.getById(idUser, new ApiResponse<User>() {
            @Override
            public void onSuccess(Resource<User> success) {
                User user = success.getData();
                user.setPassword(Constant.DEFAULT_PASSWORD);
                service.update(user, new ApiResponse<User>() {
                    @Override
                    public void onSuccess(Resource<User> success) {

                    }

                    @Override
                    public void onLoading(Resource<User> loading) {

                    }

                    @Override
                    public void onError(Resource<User> error) {

                    }
                });
            }

            @Override
            public void onLoading(Resource<User> loading) {

            }

            @Override
            public void onError(Resource<User> error) {

            }
        });
    }

    public void checkUserNameIsExisted(String userName, CallBackFunc<Boolean> callBackFunc) {
        new Thread(() -> {
            service.getAll(new ApiResponse<List<User>>() {
                @Override
                public void onSuccess(Resource<List<User>> success) {
                    List<User> list = success.getData().stream()
                            .filter(user1 -> user1.getUserName().equals(userName))
                            .collect(Collectors.toList());
                    Log.i("ADDUSER", " " + list.size());
                    if (list.size() > 0)
                        callBackFunc.success(true);
                    else
                        callBackFunc.success(false);
                }

                @Override
                public void onLoading(Resource<List<User>> loading) {

                }

                @Override
                public void onError(Resource<List<User>> error) {

                }
            });
        }).start();
    }

    public void getAll() {
        service.getAll(new ApiResponse<List<User>>() {
            @Override
            public void onSuccess(Resource<List<User>> success) {
                for (int i = 0; i < success.getData().size(); i++) {
                    Log.i("FETCH", success.getData().get(i).getFullName());
                }
            }

            @Override
            public void onLoading(Resource<List<User>> loading) {

            }

            @Override
            public void onError(Resource<List<User>> error) {
                Log.i("FETCH", error.getMessage());
            }
        });
    }
}
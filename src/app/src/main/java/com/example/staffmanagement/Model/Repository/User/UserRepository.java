package com.example.staffmanagement.MVVM.Model.Repository.User;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Entity.UserState;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.NetworkBoundResource;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.User.UserService;
import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;
import com.example.staffmanagement.MVVM.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.MVVM.Model.Ultils.UserQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    private UserService service;
    private MutableLiveData<List<User>> mLiveDataUser;
    private MutableLiveData<List<Integer>> mLiveDataQuantities;
    private MutableLiveData<List<Role>> mLiveDataRole;
    private MutableLiveData<List<UserState>> mLiveDataUserState;
    private MutableLiveData<List<User>> mLiveDataUserCheck;
    private MutableLiveData<List<String>> listFullName;

    public UserRepository() {
        service = new UserService();
        mLiveDataUser = new MutableLiveData<>();
        mLiveDataQuantities = new MutableLiveData<>();
        mLiveDataRole = new MutableLiveData<>();
        mLiveDataUserState = new MutableLiveData<>();
        mLiveDataUserCheck = new MutableLiveData<>();
        listFullName = new MutableLiveData<>();
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

    public void getLimitListUser(int idUser, int offset, int numRow, Map<String, Object> criteria) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            String q = UserQuery.getLimitListForUser(idUser, offset, numRow, criteria);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            List<User> u = AppDatabase.getDb().userDAO().getLimitListUser(sql);
            List<Integer> quantities = new ArrayList<>();
            Log.i("Count size", " " + u.size());
            for (int i = 0; i < u.size(); i++) {
                int count = new RequestRepository().getQuantityWaitingRequestForUser(u.get(i).getId());
                Log.i("Count", " " + i + " : " + count);
                quantities.add(count);
            }
            mLiveDataQuantities.postValue(quantities);
            mLiveDataUser.postValue(u);
        });
    }

    public void getAllRoleAndUserState() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            ArrayList<Role> listRole = (ArrayList<Role>) AppDatabase.getDb().roleDAO().getAll();
            return listRole;
        }).thenAccept(roles -> {
            ArrayList<UserState> listUserState = (ArrayList<UserState>) AppDatabase.getDb().userStateDAO().getAll();
            mLiveDataUserState.postValue(listUserState);
            mLiveDataRole.postValue(roles);
        });
    }

    public void populateData() {
        service.populateData();
    }

    public void updateUser(User user) {
        new Thread(() -> AppDatabase.getDb().userDAO().update(user)).start();
    }

    public User getUserForLogin(final int idUser) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> AppDatabase.getDb().userDAO().getUserById(idUser));

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getByLoginInformation(String userName, String password) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            String q = UserQuery.getUserByUserName(userName);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            User user = AppDatabase.getDb().userDAO().getUserByUserName(sql);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    return user;
                }
                return null;
            }
            return null;
        });
        try {
            return future.get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user) {
        new Thread(() -> AppDatabase.getDb().userDAO().update(user)).start();
    }

    public User insert(User user, final int idUser, final int offset, final Map<String, Object> mCriteria) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            long id = AppDatabase.getDb().userDAO().insert(user);
            String q = UserQuery.getById((int) id);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            return AppDatabase.getDb().userDAO().getById(sql);
        }).thenApply(user1 -> {
            getLimitListUser(idUser, offset, 1, mCriteria);
            return user1;
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getById(int idUser) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            String q = UserQuery.getById(idUser);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            return AppDatabase.getDb().userDAO().getById(sql);
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCount() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            String q = UserQuery.getCount();
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            return AppDatabase.getDb().userDAO().getCount(sql);
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountStaff() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            String q = UserQuery.getCountStaff();
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            return AppDatabase.getDb().userDAO().getCountStaff(sql);
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getAllStaff() {
        CompletableFuture<List<User>> future = CompletableFuture.supplyAsync(() -> {
            String q = UserQuery.getAllStaff();
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            return AppDatabase.getDb().userDAO().getAllStaff(sql);
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changeIdUserState(int idUser, int idUserState) {
        new Thread(() -> {
//            String q = UserQuery.changeIdUserState(idUser, idUserState);
//            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            AppDatabase.getDb().userDAO().changeIdUserState(idUser,idUserState);
        }).start();
    }

    public void resetPassword(int idUser) {
        new Thread(() -> {
            String q = UserQuery.resetPassword(idUser);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            AppDatabase.getDb().userDAO().resetPassword(sql);
        }).start();
    }

    public void changeAvatar(User user) {
        new Thread(() -> AppDatabase.getDb().userDAO().update(user)).start();
    }

    public boolean checkUserNameIsExisted(String userName) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            String q = UserQuery.getUserByUserName(userName);
            SimpleSQLiteQuery sql = new SimpleSQLiteQuery(q);
            return AppDatabase.getDb().userDAO().checkUserNameIsExisted(sql) != null;
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertRange(List<User> list) {
        new Thread(() -> AppDatabase.getDb().userDAO().insertRange(list)).start();
    }

    public void getAllService() {
        new NetworkBoundResource<List<User>, List<User>>() {
            @Override
            protected List<User> loadFromDb() {
                return AppDatabase.getDb().userDAO().getAll();
            }

            @Override
            protected boolean shouldFetchData(List<User> data) {
                return true; //data == null || data.size() == 0;
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
                service.getAll(apiResponse);
            }

            @Override
            protected void saveCallResult(List<User> data) {
                int count = AppDatabase.getDb().userDAO().count();
                if(count != data.size()){
                    AppDatabase.getDb().userDAO().deleteAll();
                    AppDatabase.getDb().userDAO().insertRange(data);
                }
            }

            @Override
            protected void onFetchFail(String message) {
                Log.i("FETCH", message);
            }

            @Override
            protected void onFetchSuccess(List<User> data) {
                mLiveDataUser.postValue(data);
                Log.i("FETCH", "size : " + data.size());
            }
        }.run();
    }
}
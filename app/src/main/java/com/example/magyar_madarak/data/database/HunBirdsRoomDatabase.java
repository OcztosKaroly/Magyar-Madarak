package com.example.magyar_madarak.data.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.magyar_madarak.data.dao.ObservationDAO;
import com.example.magyar_madarak.data.dao.UserDAO;
import com.example.magyar_madarak.data.dao.bird.BirdDAO;
import com.example.magyar_madarak.data.dao.constants.*;
import com.example.magyar_madarak.data.model.CrossRefTables.ColorCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.DietCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.HabitatCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.ShapeCrossRef;
import com.example.magyar_madarak.data.model.Observation;
import com.example.magyar_madarak.data.model.User;
import com.example.magyar_madarak.data.model.bird.BirdEntity;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.ConservationValue;
import com.example.magyar_madarak.data.model.constants.Diet;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;
import com.example.magyar_madarak.utils.ConverterUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

@Database(
        entities = {
                BirdEntity.class,
                Observation.class,
                User.class,
                ConservationValue.class,
                Diet.class,
                Color.class,
                Shape.class,
                Habitat.class,
                DietCrossRef.class,
                ColorCrossRef.class,
                ShapeCrossRef.class,
                HabitatCrossRef.class
        },
        version = 1,
        exportSchema = false)
@TypeConverters({ConverterUtils.class})
public abstract class HunBirdsRoomDatabase extends RoomDatabase {
    private static HunBirdsRoomDatabase instance;

    public abstract BirdDAO birdDAO();
    public abstract ObservationDAO observationDAO();
    public abstract UserDAO userDAO();

    public abstract ConservationValueDAO conservationValueDAO();
    public abstract DietDAO dietDAO();
    public abstract ColorDAO colorDAO();
    public abstract ShapeDAO shapeDAO();
    public abstract HabitatDAO habitatDAO();

    public static HunBirdsRoomDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (HunBirdsRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    HunBirdsRoomDatabase.class,
                                    "hun_birds_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(populationCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback populationCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // A feladatok egyenletesebb elosztása végett több szál lett indítva.
            //      Konstansokból kevés van, egy szálba mehetnek. Madarakból nagayon sok van, külön
            //      szálat kap. Felhasználó adatai nem foglalnak sok helyet, egyben le lehet kérni.
            Log.d("DATA", "--RoomDatabase is creating. Sync data with FireStore.--");
            Executors.newSingleThreadExecutor().execute(() -> {
                syncFirestoreDiets();
                syncFirestoreConservationValues();
                syncFirestoreColors();
                syncFirestoreShapes();
                syncFirestoreHabitats();
            });

//            Executors.newSingleThreadExecutor().execute(() -> {
//                syncFirestoreBirds();
//            });

            Executors.newSingleThreadExecutor().execute(() -> {
                syncFirestoreUsers(); // Recently this means only one user
                syncFirestoreObservations(); // This means the (only one) user's observations
            });
        }
    };

    private static void syncFirestoreDiets() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("diets")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document: queryDocumentSnapshots) {
                        Diet diet = new Diet(document.getId(), Objects.requireNonNull(document.getString("dietName")));
                        Executors.newSingleThreadExecutor().execute(() -> {
                            instance.dietDAO().insert(diet);
                            Log.d("DATA", "--Firestore diet got: " + diet.getDietName() + ".--");
                        });
                    }
                }).addOnFailureListener(e -> {
                    Log.e("DATA", "--Failed to load diets from FireBase.--", e);
                });
    }

    private static void syncFirestoreConservationValues() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("conservationValues")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document: queryDocumentSnapshots) {
                        Log.d("DATA", "--Lekérdezés eredménye: " + document);
                        ConservationValue conservationValue = new ConservationValue(document.getId(), Objects.requireNonNull(document.get("conservationValue", Integer.class)));
                        Executors.newSingleThreadExecutor().execute(() -> {
                            instance.conservationValueDAO().insert(conservationValue);
                            Log.d("DATA", "--Firestore conservation value got: " + conservationValue.getConservationValue() + ".--");
                        });
                    }
                }).addOnFailureListener(e -> {
                    Log.e("DATA", "--Failed to load conservation values from FireBase.--", e);
                });
    }

    private static void syncFirestoreColors() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("colors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document: queryDocumentSnapshots) {
                        Color color = new Color(document.getId(), Objects.requireNonNull(document.getString("colorName")));
                        Executors.newSingleThreadExecutor().execute(() -> {
                            instance.colorDAO().insert(color);
                            Log.d("DATA", "--Firestore color got: " + color.getColorName() + ".--");
                        });
                    }
                }).addOnFailureListener(e -> {
                    Log.e("DATA", "--Failed to load colors from FireBase.--", e);
                });
    }

    private static void syncFirestoreShapes() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("shapes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document: queryDocumentSnapshots) {
                        Shape shape = new Shape(document.getId(), Objects.requireNonNull(document.getString("shapeName")));
                        Executors.newSingleThreadExecutor().execute(() -> {
                            instance.shapeDAO().insert(shape);
                            Log.d("DATA", "--Firestore shape got: " + shape.getShapeName() + ".--");
                        });
                    }
                }).addOnFailureListener(e -> {
                    Log.e("DATA", "--Failed to load shapes from FireBase.--", e);
                });
    }

    private static void syncFirestoreHabitats() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("habitats")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document: queryDocumentSnapshots) {
                        Habitat habitat = new Habitat(document.getId(), Objects.requireNonNull(document.getString("habitatName")));
                        Executors.newSingleThreadExecutor().execute(() -> {
                            instance.habitatDAO().insert(habitat);
                            Log.d("DATA", "--Firestore habitat got: " + habitat.getHabitatName() + ".--");
                        });
                    }
                }).addOnFailureListener(e -> {
                    Log.e("DATA", "--Failed to load habitats from FireBase.--", e);
                });
    }

//    private static void syncFirestoreBirds() {
//        // TODO: Firestoreban a birds collectionban a name mezőt átalakítani birdName-ra a konzisztencia miatt.
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
////        firestore.collection("birds")
////                .get()
////                .addOnSuccessListener(queryDocumentSnapshots -> {
////                    for (DocumentSnapshot document: queryDocumentSnapshots) {
////
////                        BirdBase birdBase = new BirdBase(
////                                document.getId(),
////                                Objects.requireNonNull(document.getString("birdName")),
////                                Objects.requireNonNull(document.getString("mainPictureId"))
////                        );
////
////                        BirdDetail birdDetail = new BirdDetail(
////                                document.getId(),
////                                document.getString("latinName"),
////                                Boolean.TRUE.equals(document.getBoolean("migratory")),
////                                document.getString("size"),
////                                document.getString("wingSpan"),
////                                Integer.parseInt(getFireStoreReference(document.getDocumentReference("conservationValue"))),
////                                ((List<DocumentReference>) Objects.requireNonNull(document.get("diets")))
////                                        .stream()
////                                        .map(HunBirdsRoomDatabase::getFireStoreReference)
////                                        .filter(Objects::nonNull)
////                                        .collect(Collectors.toList()),
////                                ((List<DocumentReference>) Objects.requireNonNull(document.get("colors")))
////                                        .stream()
////                                        .map(HunBirdsRoomDatabase::getFireStoreReference)
////                                        .filter(Objects::nonNull)
////                                        .collect(Collectors.toList()),
////                                ((List<DocumentReference>) Objects.requireNonNull(document.get("shapes")))
////                                        .stream()
////                                        .map(HunBirdsRoomDatabase::getFireStoreReference)
////                                        .filter(Objects::nonNull)
////                                        .collect(Collectors.toList()),
////                                ((List<DocumentReference>) Objects.requireNonNull(document.get("habitats")))
////                                        .stream()
////                                        .map(HunBirdsRoomDatabase::getFireStoreReference)
////                                        .filter(Objects::nonNull)
////                                        .collect(Collectors.toList()),
////                                document.getString("description"),
////                                (List<String>) document.get("facts"),
////                                (List<String>) document.get("pictureIds")
////                        );
////
////                        Log.d("DATA", "--Firestore bird got: " + birdBase.getBirdName() + ". " +
////                                "The bird's details: " + birdDetail + ".--");
////                    }
////                }).addOnFailureListener(e -> {
////                    Log.e("DATA", "--Failed to load birds from FireBase.--", e);
////                });
//        firestore.collection("birds")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    for (DocumentSnapshot document: queryDocumentSnapshots) {
//                        Task<DocumentSnapshot> conservationValueTask = getFirestoreReference(Objects.requireNonNull(document.getDocumentReference("conservationValue")));
//                        Task<List<DocumentSnapshot>> dietsTask = getFirestoreReferences((List<DocumentReference>) document.get("diets"));
//                        Task<List<DocumentSnapshot>> colorsTask = getFirestoreReferences((List<DocumentReference>) document.get("colors"));
//                        Task<List<DocumentSnapshot>> shapesTask = getFirestoreReferences((List<DocumentReference>) document.get("shapes"));
//                        Task<List<DocumentSnapshot>> habitatsTask = getFirestoreReferences((List<DocumentReference>) document.get("habitats"));
//
//                        Tasks.whenAllSuccess(conservationValueTask, dietsTask, colorsTask, shapesTask, habitatsTask)
//                                .addOnSuccessListener(aVoid -> {
//                                    DocumentSnapshot conservationValue = conservationValueTask.getResult();
//                                    List<DocumentSnapshot> diets = dietsTask.getResult();
//                                    List<DocumentSnapshot> colors = colorsTask.getResult();
//                                    List<DocumentSnapshot> shapes = shapesTask.getResult();
//                                    List<DocumentSnapshot> habitats = habitatsTask.getResult();
//
//                                    BirdBase birdBase = new BirdBase(
//                                            document.getId(),
//                                            Objects.requireNonNull(document.getString("birdName")),
//                                            Objects.requireNonNull(document.getString("mainPictureId"))
//                                    );
//
//                                    BirdDetail birdDetail = new BirdDetail(
//                                            document.getId(),
//                                            document.getString("latinName"),
//                                            Boolean.TRUE.equals(document.getBoolean("migratory")),
//                                            document.getString("size"),
//                                            document.getString("wingSpan"),
//                                            conservationValue,
//                                            diets,
//                                            colors,
//                                            shapes,
//                                            habitats,
//                                            document.getString("description"),
//                                            (List<String>) document.get("facts"),
//                                            (List<String>) document.get("pictureIds")
//                                    );
//
//                                    Log.d("DATA", "--Firestore bird got: " + birdBase.getBirdName() + ". " +
//                                            "The bird's details: " + birdDetail + ".--");
//                                })
//                                .addOnFailureListener(e -> {
//                                    Log.e("DATA", "--Failed to load references.--", e);
//                                });
//
//                    }
//                }).addOnFailureListener(e -> {
//                    Log.e("DATA", "--Failed to load birds from FireBase.--", e);
//                });
//    }

    private static Task<DocumentSnapshot> getFirestoreReference(DocumentReference reference) {
        return reference.get();
    }

    private static Task<List<DocumentSnapshot>> getFirestoreReferences(List<DocumentReference> references) {
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        for (DocumentReference reference : references) {
            tasks.add(reference.get());
        }

        return Tasks.whenAllSuccess(tasks)
                .continueWith(task -> {
                    List<DocumentSnapshot> snapshots = new ArrayList<>();
                    for (Task<DocumentSnapshot> individualTask : tasks) {
                        if (individualTask.isSuccessful()) {
                            snapshots.add(individualTask.getResult());
                        } else {
                            snapshots.add(null);
                        }
                    }
                    return snapshots;
                });
    }

    // TODO
    private static void syncFirestoreUsers() {
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        // TODO: ha még nincs visszaigazolva az email cím, akkor nem lehet a felhasználónak adata.
//        if (currentUser != null) {
//            firestore.collection("users")
//                    .document(currentUser.getUid())
//                    .get()
//                    .addOnSuccessListener(document -> {
//                        UserBase userBase = new UserBase(currentUser.getUid(), Objects.requireNonNull(currentUser.getEmail()));
//                        UserDetail userDetail = new UserDetail(
//                                currentUser.getUid(),
//                                currentUser.isEmailVerified(),
//                                Boolean.TRUE.equals(document.getBoolean("saveOnlineObservations")),
//                                Boolean.TRUE.equals(document.getBoolean("notificationsEnabled"))
//                        );
//                        Log.d("DATA", "--Firestore user got: " + userBase.getEmail() + ". " +
//                                "The user's settings: " + userDetail + "--");
//                    }).addOnFailureListener(e -> {
//                        Log.e("DATA", "--Failed to load user from FireBase.--", e);
//                    });
//        } else {
//            Log.w("DATA", "--User data could not be loaded because no user is authenticated.--");
//        }
    }

    // TODO
    private static void syncFirestoreObservations() {
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//
//        // TODO: ha nincs bejelentkezve a felhasználó, akkor nem tud letölteni adatokat
//        if (currentUser != null) {
//            firestore.collection("observations")
//                    .whereEqualTo("userId", currentUser.getUid())
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        for (DocumentSnapshot document: queryDocumentSnapshots) {
//                            ObservationBase observationBase = new ObservationBase(
//                                    document.getId(),
//                                    Objects.requireNonNull(document.getString("userId")),
//                                    Objects.requireNonNull(document.getString("description"))
//                            );
//                            ObservationDetail observationDetail = new ObservationDetail(
//                                    document.getId(),
//                                    Objects.requireNonNull(document.getString("userId")),
//                                    document.getString("createdAt"),
//                                    document.getString("lastModified"),
//                                    document.getString("observationDate"),
//                                    document.getString("observationLocation"),
//                                    document.getString("observationDescription"),
//                                    (List<String>) document.get("observedBirdColors"),
//                                    (List<String>) document.get("observedBirdShapess"),
//                                    (List<String>) document.get("observedBirdHabitats")
//                            );
//                            Log.d("DATA", "--Firestore observation got: " + observationBase.getObservationName() + ". " +
//                                    "The observation's details: " + observationDetail + "--");
//                        }
//                    }).addOnFailureListener(e -> {
//                        Log.e("DATA", "--Failed to load observations from FireBase.--", e);
//                    });
//        } else {
//            Log.w("DATA", "--Observation data could not be loaded because no user is authenticated.--");
//        }
    }
}

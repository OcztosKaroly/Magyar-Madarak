package com.example.magyar_madarak.data.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.magyar_madarak.HunBirdApplication;
import com.example.magyar_madarak.data.dao.observation.ObservationDAO;
import com.example.magyar_madarak.data.dao.user.UserDAO;
import com.example.magyar_madarak.data.dao.bird.BirdDAO;
import com.example.magyar_madarak.data.dao.constants.*;
import com.example.magyar_madarak.data.model.crossRefTables.ColorCrossRef;
import com.example.magyar_madarak.data.model.crossRefTables.DietCrossRef;
import com.example.magyar_madarak.data.model.crossRefTables.HabitatCrossRef;
import com.example.magyar_madarak.data.model.crossRefTables.ShapeCrossRef;
import com.example.magyar_madarak.data.model.observation.Observation;
import com.example.magyar_madarak.data.model.user.User;
import com.example.magyar_madarak.data.model.bird.Bird;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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

            Executors.newSingleThreadExecutor().execute(() -> {
                syncFirestoreBirds();
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

    public static void syncFirestoreBirds() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("birds")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document: queryDocumentSnapshots) {
                        String conservationReferencePath = Objects.requireNonNull(document.getDocumentReference("conservationValue")).getPath();
                        String conservationId = conservationReferencePath.substring(conservationReferencePath.lastIndexOf('/') + 1);

                        List<String> dietIds = getFirestoreReferences((List<DocumentReference>) document.get("diets"));
                        List<String> colorIds = getFirestoreReferences((List<DocumentReference>) document.get("colors"));
                        List<String> shapeIds = getFirestoreReferences((List<DocumentReference>) document.get("shapes"));
                        List<String> habitatIds = getFirestoreReferences((List<DocumentReference>) document.get("habitats"));

                        if (dietIds != null && colorIds != null && shapeIds != null && habitatIds != null) {
                            List<Task<DocumentSnapshot>> dietTasks = createTasks(firestore, "diets", dietIds);
                            List<Task<DocumentSnapshot>> colorTasks = createTasks(firestore, "colors", colorIds);
                            List<Task<DocumentSnapshot>> shapeTasks = createTasks(firestore, "shapes", shapeIds);
                            List<Task<DocumentSnapshot>> habitatTasks = createTasks(firestore, "habitats", habitatIds);

                            Task<List<Object>> allTasks = Tasks.whenAllSuccess(dietTasks)
                                    .continueWithTask(task -> Tasks.whenAllSuccess(colorTasks))
                                    .continueWithTask(task -> Tasks.whenAllSuccess(shapeTasks))
                                    .continueWithTask(task -> Tasks.whenAllSuccess(habitatTasks));

                            allTasks.addOnSuccessListener(result -> {
                                List<Diet> diets = dietTasks.stream()
                                        .map(dietDoc -> new Diet(dietDoc.getResult().getId(),
                                                Objects.requireNonNull(dietDoc.getResult().getString("dietName"))))
                                        .collect(Collectors.toList());

                                List<Color> colors = colorTasks.stream()
                                        .map(colorDoc -> new Color(colorDoc.getResult().getId(),
                                                Objects.requireNonNull(colorDoc.getResult().getString("colorName"))))
                                        .collect(Collectors.toList());

                                List<Shape> shapes = shapeTasks.stream()
                                        .map(shapeDoc -> new Shape(shapeDoc.getResult().getId(),
                                                Objects.requireNonNull(shapeDoc.getResult().getString("shapeName"))))
                                        .collect(Collectors.toList());

                                List<Habitat> habitats = habitatTasks.stream()
                                        .map(habitatDoc -> new Habitat(habitatDoc.getResult().getId(),
                                                Objects.requireNonNull(habitatDoc.getResult().getString("habitatName"))))
                                        .collect(Collectors.toList());

                                Bird bird = new Bird(new BirdEntity(
                                        document.getId(),
                                        Objects.requireNonNull(document.getString("birdName")),
                                        document.getString("mainPictureId"),
                                        document.getString("latinName"),
                                        Boolean.TRUE.equals(document.getBoolean("migratory")),
                                        document.getString("size"),
                                        document.getString("wingSpan"),
                                        conservationId,
                                        document.getString("description"),
                                        (List<String>) document.get("facts"),
                                        (List<String>) document.get("pictureIds"))
                                );

                                bird.setDiets(diets);
                                bird.setColors(colors);
                                bird.setShapes(shapes);
                                bird.setHabitats(habitats);

                                Executors.newSingleThreadExecutor().execute(() -> {
                                    instance.birdDAO().insert(bird);
//                                    syncBirdImage(bird.getMainPictureId());
                                    syncBirdImage(bird.getBirdName());
                                    Log.d("DATA", "--Firestore bird got: " + bird.getBirdName() + ".--");
                                });
                            });
                        }
                    }
                });
    }

    private static List<String> getFirestoreReferences(List<DocumentReference> references) {
        if (references != null) {
            return references.stream()
                    .filter(Objects::nonNull)
                    .map(reference -> reference.getPath().substring(reference.getPath().lastIndexOf('/') + 1))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private static List<Task<DocumentSnapshot>> createTasks(FirebaseFirestore firestore, String collectionName, List<String> ids) {
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String id : ids) {
            tasks.add(firestore.collection(collectionName).document(id).get());
        }
        return tasks;
    }

    private static void syncBirdImage(String mainPictureId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference pictureRef = storage.getReference().child("main_bird_images").child(mainPictureId.toLowerCase() + ".jpg");

        pictureRef.getBytes(512 * 512).addOnSuccessListener(bytes -> {
            try {
                File directory = new File(HunBirdApplication.getAppContext().getFilesDir(), "main_bird_images");
                directory.mkdirs();

                File file = new File(directory, mainPictureId + ".jpg");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.close();
            } catch (IOException e) {
                Log.e("DATA", "--Failed to save image.--", e);
            }
        }).addOnFailureListener(e -> {
            Log.e("DATA", "--Failed to download image: " + mainPictureId + ".--", e);
            Log.e("DATA", "PictureRef: " + pictureRef);
        });
    }
}

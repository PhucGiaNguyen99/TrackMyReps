package com.example.workouttracker;


import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import com.example.workouttracker.database.WorkoutDatabaseHelper;
import com.example.workouttracker.models.Exercise;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FirestoreSyncHelperTest {

    @Mock
    FirebaseFirestore mockFirestore;

    @Mock
    WorkoutDatabaseHelper mockDbHelper;

    @Mock
    CollectionReference mockCollectionRef;

    @Mock
    DocumentReference mockDocRef;

    @Mock
    Task<QuerySnapshot> mockTask;

    FirestoreSyncHelper syncHelper;

    @Before
    public void setUp() {
        syncHelper = new FirestoreSyncHelper(mockFirestore, mockDbHelper);
    }

    @Test
    public void testSyncExercisesSuccess() {
        String userId = "testUser";
        QuerySnapshot mockSnapshot = Mockito.mock(QuerySnapshot.class);
        List<DocumentSnapshot> docs = new ArrayList<>();
        DocumentSnapshot doc = Mockito.mock(DocumentSnapshot.class);
        Exercise dummyExercise = new Exercise("Pushup", 3, 12, 0);
        Mockito.when(doc.toObject(Exercise.class)).thenReturn(dummyExercise);
        docs.add(doc);
        Mockito.when(mockSnapshot.getDocuments()).thenReturn(docs);

        Mockito.when(mockFirestore.collection("users")).thenReturn(mockCollectionRef);
        Mockito.when(mockCollectionRef.document(userId)).thenReturn(mockDocRef);
        Mockito.when(mockDocRef.collection("exercises")).thenReturn(mockCollectionRef);
        Mockito.when(mockCollectionRef.get()).thenReturn(Tasks.forResult(mockSnapshot));

        syncHelper.syncExercisesFromFirestore(userId, (success, error) -> {
            assertTrue(success);
            assertNull(error);
            verify(mockDbHelper).addExercise(dummyExercise);
        });
    }
}


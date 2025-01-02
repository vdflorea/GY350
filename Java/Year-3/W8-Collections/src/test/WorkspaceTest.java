/**
 * This class performs various tests to ensure
 * desired User Workspace functionality
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package test;

import main.UserAccount;
import main.Workspace;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkspaceTest extends BaseTest {
    @Test
    public void testToString() {
        Workspace w = new Workspace("Workspace", "Cool Workspace", users.getFirst());
        w.addCollaborator(users.getLast());

        String testString = "--------------- Workspace ---------------\n" +
                            "Workspace Description: Cool Workspace\n" +
                            "--- Owner Account ---\n" +
                            users.getFirst().toString() +
                            "--- Collaborator #1 ---\n" +
                            users.getLast().toString() +
                            "---------------------\n";
        assertEquals(testString, w.toString());
    }

    @Test
    public void testMapUsersWorkspaces() {
        Map<UserAccount, List<Workspace>> usersWorkspacesMap = new HashMap<>();

        Workspace w1 = new Workspace("Workspace A1", "Interactive Workspace A1", users.get(0));
        w1.addCollaborator(users.get(4));
        w1.addCollaborator(users.get(7));
        Workspace w2 = new Workspace("Workspace A2", "Interactive Workspace A2", users.get(0));
        w2.addCollaborator(users.get(8));

        List<Workspace> user0Workspaces = new ArrayList<>();
        user0Workspaces.add(w1);
        user0Workspaces.add(w2);

        // Add each UserAccount with an empty list of Workspaces
        for (UserAccount user : users) {
            if (user.equals(users.getFirst())) {
                usersWorkspacesMap.put(users.getFirst(), user0Workspaces);
            } else {
                usersWorkspacesMap.put(user, new ArrayList<>()); // Add an empty list
            }
        }

        System.out.println(usersWorkspacesMap.get(users.getFirst()).toString());
        System.out.println("\nExample: User at index 3 in users list has no mapped workspaces currently: " + usersWorkspacesMap.get(users.get(3)));
    }
}

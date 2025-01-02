/**
 * This class represents a Workspace on
 * an online application
 *
 * @author Vlad Florea 22409144
 * @version 1.0
 */

package main;

import java.util.ArrayList;
import java.util.List;

public class Workspace {
    private String workspaceName;
    private String workspaceDescription;
    private UserAccount owner;
    private List<UserAccount> collaborators;

    /**
     * @param workspaceName The chosen name of the workspace
     * @param workspaceDescription The description for the workspace
     * @param owner The user who owns/created the workspace
     */
    public Workspace(String workspaceName, String workspaceDescription, UserAccount owner) {
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
        this.owner = owner;
        collaborators = new ArrayList<>();
    }

    /**
     * Creates a String representation of the details of a workspace
     * -> (Including its collaborators)
     *
     * @return String representation of a workspace
     */
    @Override
    public String toString() {
        String out = String.format("--------------- %s ---------------\nWorkspace Description: %s\n--- Owner Account ---\n%s", workspaceName, workspaceDescription, owner.toString());

        // Print out each collaborator's details individually
        int num = 1;
        for (UserAccount collaborator : collaborators) {
            out += "--- Collaborator #" + num + " ---\n" + collaborator.toString() + "---------------------\n";
            num++;
        }

        return out;
    }

    public void addCollaborator(UserAccount collaborator) {
        collaborators.add(collaborator);
    }

    public void removeCollaborator(UserAccount collaborator) {
        collaborators.remove(collaborator);
    }
}

using UnityEngine;

public class BoundarySpawner : MonoBehaviour
{
    [SerializeField] private GameObject nextBoundary;
    
    private GameObject _originalBoundary;
    private bool _isSpawned;

    private void Start()
    {
        _originalBoundary = nextBoundary;
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.CompareTag("Spaceship")) 
        {   
            // Spaceship has entered the "collision box" in the centre of the invisible boundary
            if (!_isSpawned)
            {
                // Spawn the next boundary at the NextBoundarySpawnPoint object's position
                nextBoundary = _originalBoundary;
                Instantiate(nextBoundary,transform.GetChild(0).transform.position, Quaternion.identity);
                _isSpawned = true;
                
                // Free up game resources by destroying previous boundaries 
                Invoke("DestroyBoundary", 10f);
            }
        }
    }

    private void DestroyBoundary()
    {
        // Destroy the invisible boundary and all of its children
        Destroy(transform.parent.gameObject);
    }
}

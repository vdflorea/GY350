using UnityEngine;

public class Asteroid : MonoBehaviour
{
    private float _randomScaler;
    private Rigidbody _rb;
    private bool _hitOnce;
    
    [SerializeField] private float rotationSpeed = 10000f;
    [SerializeField] private GameObject explosion;
    [SerializeField] private GameObject[] asteroidFragmentsPrefabs;
    
    private void Start()
    {
        _rb = GetComponent<Rigidbody>();
        
        // Set random size for asteroid
        _randomScaler = Random.Range(3f, 5f);
        transform.localScale = new Vector3(_randomScaler, _randomScaler, _randomScaler); 
        
        // Spin/rotate asteroid in a random direction
        Vector3 randomDirection = new Vector3(
            Random.Range(-1f, 1f),
            Random.Range(-1f, 1f),
            Random.Range(-1f, 1f)
        ).normalized;
        _rb.AddTorque(randomDirection * rotationSpeed);
    }

    private void OnCollisionEnter(Collision collision)
    {
        if (!collision.gameObject.CompareTag("Bullet") && !collision.gameObject.CompareTag("Spaceship")) return;
        
        if (collision.gameObject.CompareTag("Bullet") && !_hitOnce)
        {
            // If two bullets hit an asteroid at the same time, ensure score is only updated once
            GameManager.instance.UpdateScore();
            _hitOnce = true;
        }
            
        // Select a random asteroid fragment prefab
        GameObject asteroidFragmentPrefab = Instantiate(asteroidFragmentsPrefabs[Random.Range(0, asteroidFragmentsPrefabs.Length)],
            transform.position, transform.rotation, transform.parent);
            
        // Scale the whole object (which contains multiple fragments) to the size of the original asteroid
        asteroidFragmentPrefab.transform.localScale = new Vector3(_randomScaler, _randomScaler, _randomScaler);
            
        // Spin/rotate asteroid fragments in direction of collision contact point
        foreach (Transform child in asteroidFragmentPrefab.transform)
        {
            Vector3 torqueDirection = (child.position - collision.contacts[0].point).normalized; // Calculate direction
            child.GetComponent<Rigidbody>().AddTorque(torqueDirection * rotationSpeed);
        }
            
        // Spawn an explosion at collision point
        Instantiate(explosion, collision.contacts[0].point, Quaternion.identity);
            
        SoundManager.instance.PlayExplodeSound(); 
        Destroy(gameObject);
    }
}

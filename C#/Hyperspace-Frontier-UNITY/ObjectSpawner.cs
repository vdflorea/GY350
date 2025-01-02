using UnityEngine;

public class ObjectSpawner : MonoBehaviour
{
    [SerializeField] private GameObject[] asteroidPrefabs;
    [SerializeField] private GameObject[] powerUpPrefabs;
    [SerializeField] private GameObject[] debuffPrefabs;
    
    private static int _numObstaclesPerBox = 10;
    private static int _numPowerUpsPerBox = 4;
    private static int _numDebuffsPerBox = 2;
    
    private const int MaxObstaclesPerBox = 60; 
    private const int MinPowerUpsPerBox = 2;
    private const int MaxDebuffsPerBox = 8;
    private const float BufferDistance = 50f;
    
    private Vector3 _boxSize;
    private GameObject _objectParent;


    private void Start()
    {
        // Increment wave number for each new boundary spawned
        GameManager.instance.waveNumber++;
        Debug.Log("Wave: " + GameManager.instance.waveNumber);
        
        // Initial wave setup
        CalculateBoxSize();
        InstantiateNewObjectSpawner();
        IncreaseWaveDifficulty();

        // Spawn objects
        SpawnObstacles();
        SpawnPowerUps();
        SpawnDebuffs();
    }

    private void CalculateBoxSize()
    {
        // Distance from left plane to right plane of bounding box
        float width = Vector3.Distance(transform.GetChild(1).position, transform.GetChild(0).position); 
        
        // Distance from bottom plane to top plane of bounding box
        float height = Vector3.Distance(transform.GetChild(2).position, transform.GetChild(3).position); 
        
        // Distance from middle of box to front
        float length = Vector3.Distance(transform.GetChild(4).position, transform.GetChild(5).position); 
        
        _boxSize = new Vector3(width, height, length);
    }

    private void InstantiateNewObjectSpawner()
    {
        // Instantiate a new "ObjectSpawner" object as a child of the invisible boundary
        GameObject temp = new GameObject("ObjectSpawner");
        _objectParent = Instantiate(temp, transform.GetChild(6).transform.position, Quaternion.identity, transform);
        DestroyImmediate(temp);
       
        // Destroy the parent ObjectSpawner of all the objects from the previous boundary
        Destroy(transform.GetChild(6).gameObject); 
    }

    private void IncreaseWaveDifficulty()
    {
        
        // Each wave, increment the number of obstacles spawned in the box (until maxed out)
        if (_numObstaclesPerBox != MaxObstaclesPerBox)
        {
            _numObstaclesPerBox += 2;
        }

        if (_numPowerUpsPerBox != MinPowerUpsPerBox)
        {
            // Every 9 waves, decrement the number of power-ups in the box (until reach minimum)
            if (GameManager.instance.waveNumber % 9 == 0)
            {
                _numPowerUpsPerBox--;
            }
        }

        if (_numDebuffsPerBox != MaxDebuffsPerBox)
        {
            // Every three waves, increment the number of debuffs in the box (until maxed out)
            if (GameManager.instance.waveNumber % 3 == 0)
            {
                _numDebuffsPerBox++;
            }
        }
    }

    private void SpawnObstacles()
    {
        for (int i = 0; i < _numObstaclesPerBox; i++)
        {
            // Select a random asteroid prefab
            GameObject asteroidPrefab = asteroidPrefabs[Random.Range(0, asteroidPrefabs.Length)];
    
            Instantiate(asteroidPrefab, transform.position + CalculatePosition(), Quaternion.identity, _objectParent.transform);
        }
    }

    private void SpawnPowerUps()
    {
        for (int i = 0; i < _numPowerUpsPerBox; i++)
        {
            // Select a random power-up
            GameObject powerUpPrefab = powerUpPrefabs[Random.Range(0, powerUpPrefabs.Length)];
            
            Instantiate(powerUpPrefab, transform.position + CalculatePosition(), Quaternion.identity, _objectParent.transform);
        }
    }

    private void SpawnDebuffs()
    {
        for (int i = 0; i < _numDebuffsPerBox; i++)
        {
            // Select a random debuff
            GameObject debuffPrefab = debuffPrefabs[Random.Range(0, debuffPrefabs.Length)];
            
            Instantiate(debuffPrefab, transform.position + CalculatePosition(), Quaternion.identity, _objectParent.transform);
        }
    }

    private Vector3 CalculatePosition()
    {
        // Random position within box
        return new Vector3(
            Random.Range(-_boxSize.x / 2, _boxSize.x / 2),
            Random.Range(-_boxSize.y / 2 + BufferDistance, _boxSize.y - BufferDistance),
            Random.Range(-_boxSize.z, _boxSize.z)
        );
    }

    public static void Reset()
    {
        // Reset static variables after each game
        _numObstaclesPerBox = 10;
        _numPowerUpsPerBox = 4;
        _numDebuffsPerBox = 2;
    }
}

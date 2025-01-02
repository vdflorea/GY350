using UnityEngine;

public class Spaceship : MonoBehaviour
{
    public float speed = 500f;
    public float fireRate = 0.25f;
    
    [SerializeField] private float rotationSpeed = 50f;
    [SerializeField] private GameObject bulletPrefab;
    [SerializeField] private Transform boundary;
    [SerializeField] private bool invincible;
    
    [SerializeField] private float maxYaw = 30f;
    [SerializeField] private float maxPitch = 15f;
    [SerializeField] private float maxRoll = 15f;
    
    private float _currentYaw;
    private float _currentPitch;
    private float _currentRoll;
    
    private float _nextFireTime;
    private Rigidbody _rb;

    private void Start()
    {
        _rb = GetComponent<Rigidbody>();
    }

    private void Update()
    {
        // Game starts once player is no longer in the menu AND spaceship has taken off
        if (!GameManager.instance.menuUI.activeSelf && GameManager.instance.hasSpaceshipTakenOff)
        { 
            // Set the velocity each frame
            _rb.velocity = -transform.forward * (speed * GameManager.instance.speedMultiplier);
            
            // Get raw input values for vertical and horizontal axes
            float inputAxisHorizontal = Input.GetAxisRaw("Horizontal");
            float inputAxisVertical = Input.GetAxisRaw("Vertical");
            
            // Handle yaw (left/right rotation)
            if (Mathf.Abs(inputAxisHorizontal) > 0.1f)
            {
                float targetYaw = inputAxisHorizontal * maxYaw;
                _currentYaw = Mathf.MoveTowards(_currentYaw, targetYaw, rotationSpeed * Time.deltaTime);
            }

            // Handle pitch (up/down rotation)
            if (Mathf.Abs(inputAxisVertical) > 0.1f)
            {
                float targetPitch = -inputAxisVertical * maxPitch;
                _currentPitch = Mathf.MoveTowards(_currentPitch, targetPitch, rotationSpeed * Time.deltaTime);
            }
      
            // Handle roll (tiling left/right)
            if (Mathf.Abs(inputAxisHorizontal) > 0.1f)
            {
                float targetRoll = -inputAxisHorizontal * maxRoll;
                _currentRoll = Mathf.MoveTowards(_currentRoll, targetRoll, rotationSpeed * Time.deltaTime);
            }
            
            // Apply the rotation using calculated pitch, yaw and roll values
            Quaternion targetRotation = Quaternion.Euler(_currentPitch, _currentYaw, _currentRoll);
            transform.localRotation = Quaternion.Slerp(transform.localRotation, targetRotation, 10f * Time.deltaTime);
            
            // Shoot bullet
            if (Input.GetKey(KeyCode.Space) && Time.time >= _nextFireTime) 
            {
                SoundManager.instance.PlayRailgunSound(); // Play sound
                
                // Instantiate bullet at top-left barrel of spaceship
                Instantiate(bulletPrefab, transform.GetChild(3).transform.position, transform.GetChild(3).transform.rotation);
                
                // Instantiate bullet at top-right barrel of spaceship
                Instantiate(bulletPrefab, transform.GetChild(4).transform.position, transform.GetChild(4).transform.rotation); 
                
                // Update the next time when spaceship can fire again
                _nextFireTime = Time.time + (fireRate * GameManager.instance.fireRateMultiplier);
            }
            
            // Ensure that spaceship stays within invisible boundary walls
            float spaceshipX = Mathf.Clamp(transform.position.x, boundary.GetChild(0).transform.position.x, boundary.GetChild(1).transform.position.x);
            float spaceshipY = Mathf.Clamp(transform.position.y, boundary.GetChild(2).transform.position.y, boundary.GetChild(3).transform.position.y);
            transform.position = new Vector3(spaceshipX, spaceshipY, transform.position.z);
        }
    }

    private void OnCollisionEnter(Collision collision)
    {
        if (!collision.gameObject.CompareTag("Asteroid") || invincible) return;
        
        // Game over!
        GameManager.instance.isGameOver = true;
        GameManager.instance.RestartGameAfterDelay(3f);
        Destroy(gameObject);
    }

}

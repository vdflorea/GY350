using UnityEngine;

public class MoveWithSpaceship : MonoBehaviour
{
    [SerializeField] private Transform spaceship;
    
    private ParticleSystem _particleSystem;
    private float _originalX;
    private float _originalY;

    private void Start()
    {
        _originalX = transform.position.x;
        _originalY = transform.position.y;
        _particleSystem = GetComponentInChildren<ParticleSystem>();
        _particleSystem.Stop(); // Stop particles during take-off/launch sequence
    }

    private void Update()
    {
        if (GameManager.instance.isGameOver) return;
        
        if (GameManager.instance.startParticles)
        {
            // Particles will start just before spaceship takes off
            _particleSystem.Play();
            GameManager.instance.startParticles = false;
        }
        
        if (GameManager.instance.timeToPlay)
        {
            // Spaceship has taken off, animation has finished; Now it's time to play 
            transform.position = new Vector3(_originalX, _originalY, spaceship.position.z - 1000f);
        }
    }
}

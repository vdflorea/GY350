using UnityEngine;

public class Bullet : MonoBehaviour
{
    [SerializeField] private float speed = 1500f;
    private GameObject _spaceship;
    private Vector3 _targetPosition;

    private void Start()
    {
        _spaceship = GameObject.Find("Spaceship");
        
        // Set target position to the spaceship's crosshair
        _targetPosition = _spaceship.transform.GetChild(5).position; 
    }

    private void Update()
    {
        // Move the bullet towards the crosshair each frame
        // NOTE: Bullet will auto-destroy when reaching the crosshair (AutoDestroy script)
        transform.localPosition = Vector3.MoveTowards(transform.localPosition, _targetPosition, Time.deltaTime * speed);
    }
}

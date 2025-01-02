using UnityEngine;

public class ThirdPersonCamera : MonoBehaviour
{
    [SerializeField] Transform spaceship;
    [SerializeField] private Vector3 cameraOffset = new (0, 6, 50);
    [SerializeField] private float transitionSpeed = 2f;
    
    private Vector3 _prelaunchPosition;
    
    private bool _isTransitioningToPrelaunch = true;
    private bool _playedTakeOffSound;
    
    private const float MaxShakeIntensity = 0.5f;
    private const float ShakeDuration = 3f;
    private bool _isShaking;
    private float _shakeIntensity;
    private float _shakeTimer;

    private void Start()
    {
        // Specify a position for the camera before spaceship take-off
        _prelaunchPosition = spaceship.position + cameraOffset;
    }

    private void Update()
    {
        if (!GameManager.instance.menuUI.activeSelf && !GameManager.instance.isGameOver)
        {
            if (!GameManager.instance.hasSpaceshipTakenOff)
            {
                if (_isTransitioningToPrelaunch)
                {
                    // STEP 1: Move camera towards pre-launch position
                    TransitionToPrelaunch();
                    if (!_playedTakeOffSound)
                    {
                        SoundManager.instance.PlayTakeOffSound();
                        _playedTakeOffSound = true;
                    }
                }
                else if (_isShaking)
                {
                    // STEP 2: Camera is now in pre-launch position, start to take off (shake camera)
                    ApplyCameraShake();
                }
            }
            else if (GameManager.instance.timeToPlay)
            {
                // STEP 3: Spaceship has taken off, start actual game
                FollowSpaceship();
            }
        }
    }

    private void TransitionToPrelaunch()
    {
        // Smoothly move camera towards pre-launch position (while looking at spaceship's crosshair)
        transform.position = Vector3.Lerp(transform.position, _prelaunchPosition, transitionSpeed * Time.deltaTime);
        Quaternion targetRotation = Quaternion.LookRotation(spaceship.GetChild(5).position - transform.position);
        transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, transitionSpeed * Time.deltaTime);

        if (Vector3.Distance(transform.position, _prelaunchPosition) < 0.1f)
        {
            // Camera is now in pre-launch position, start take-off sequence
            _isTransitioningToPrelaunch = false;
            _isShaking = true;
            Invoke("StartHyperspaceEffectWrapper", ShakeDuration - 1f);
            Invoke("TakeOffWrapper", ShakeDuration);
        }
    }

    private void ApplyCameraShake()
    {
        // Shake the camera and increase intensity over time
        _shakeTimer += Time.deltaTime;
        float shakeProgress = _shakeTimer / ShakeDuration;
        _shakeIntensity = Mathf.Lerp(0, MaxShakeIntensity, shakeProgress);
        
        // Generate a random offset for the camera position and apply it
        Vector3 shakeOffset = Random.insideUnitSphere * _shakeIntensity;
        transform.position = _prelaunchPosition + shakeOffset;
        
        // Check if shake duration has elapsed
        if (_shakeTimer >= ShakeDuration)
        {
            _isShaking = false;
        }
    }

    private void FollowSpaceship()
    {
        // Ensure camera follows behind spaceship from a specified distance (offset)
        Vector3 targetPosition = spaceship.position + cameraOffset;
        transform.position = targetPosition;
        
        // Smoothly rotate camera towards spaceship's crosshair
        Quaternion targetRotation = Quaternion.LookRotation(spaceship.GetChild(5).position - transform.position);
        transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, Time.deltaTime / 2f);
        
    }
    
    private void StartHyperspaceEffectWrapper()
    {
        GameManager.instance.StartHyperspaceEffect();
    }

    private void TakeOffWrapper()
    {
        GameManager.instance.TakeOff();
    }
}


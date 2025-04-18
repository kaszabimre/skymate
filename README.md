# skymate
A cross-platform weather application built with Kotlin Multiplatform.

## Notes

If I had more time, I would have implemented the following:

- Finished the `LocationService` implementation on iOS.
- Added permission handling, including:
    - Showing a dialog if location permission is not granted.
    - Navigating the user to system settings when necessary.
- Wrote more tests for both the ViewModels and the service layer.
- Improved error handling with custom error messages by mapping API errors to domain-specific errors.
- Introduced further modularization to better separate concerns and improve maintainability.
- Added a repository layer for cleaner architecture, which would be especially beneficial in a more complex application.

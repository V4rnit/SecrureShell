Contributing to SecureShell

Thank you for your interest in contributing to SecureShell. This document provides guidelines for contributing.

Getting Started

1. Fork the repository
2. Clone your fork
3. Create a feature branch
4. Make your changes
5. Write tests for new features
6. Ensure all tests pass
7. Submit a pull request

Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and single-purpose
- Handle exceptions appropriately

Adding New Commands

To add a new command:

1. Create a class implementing the Command interface
2. Implement execute(), getName(), and getDescription() methods
3. Register the command in CommandRegistry
4. Write unit tests for the command
5. Update the help text in SecureShell.java

Testing

- Write unit tests for all new features
- Ensure test coverage for edge cases
- Run tests before submitting: mvn test

Pull Request Process

1. Update documentation as needed
2. Add tests for new functionality
3. Ensure the build passes
4. Update CHANGELOG.md if applicable
5. Request review from maintainers

Questions

If you have questions, please open an issue for discussion.

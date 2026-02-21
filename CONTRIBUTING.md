# Contributing to RevWorkForce

This document contains the guidelines that all team members must follow for development of RevWorkForce Phase 2 Project.
## Getting Started

1. **Fork the repository**
2. **Clone your fork**
```bash
   git clone https://github.com/RevWorkForce-PES/P2-RevWorkForce.git
```
3. **Add upstream remote**
```bash
   git remote add upstream https://github.com/RevWorkForce-PES/P2-RevWorkForce.git
```

## Development Workflow

### 1. Create a Feature Branch
```bash
git checkout main
git pull upstream main
git checkout -b feature/add-your-feature-name
```

Branch naming conventions:
- `feature/` - New features
- `bugfix/` - Bug fixes
- `hotfix/` - Urgent production fixes
- `test/` - Test additions
- `docs/` - Documentation

### 2. Make Changes

- Write clean, readable code
- Follow Java coding standards
- Add JavaDoc comments
- Write unit tests (aim for 60%+ coverage)
- Test your changes locally

### 3. Commit Changes
```bash
git add .
git commit -m "feat: add employee search functionality"
```

Commit message format:
```
<type>: <description>
```

Types:
- `feat` - New feature
- `fix` - Bug fix
- `docs` - Documentation
- `style` - Formatting
- `refactor` - Code restructuring
- `test` - Tests
- `chore` - Build/config

### 4. Keep Your Branch Updated
```bash
git fetch upstream
```

### 5. Push Changes
```bash
git push origin feature/your-feature-name
```

### 6. Create Pull Request

1. Go to your fork GitHub repository
2. Click "Contribute/New Pull Request"
3. Select your branch
4. Add PR title and PR description with your changes
5. Request review from team members

## Pull Request Guidelines

### PR Title Format
```
[Module] Brief description

Examples:
[Auth] Implement login functionality
[Leave] Fix balance calculation bug
[UI] Update dashboard layout
```

### PR Description Template
```markdown
## Description
Brief description of changes

## Type of Change
- Bug fix
- New feature
- Breaking change
- Documentation update

## Add Related Issue
- Closes #[issue-number]

## Checklist
- Code follows style guidelines
- Self-review completed
- Comments added to complex code
- Documentation updated
- No new warnings
```

### Responding to Reviews

- Be open to feedback
- Respond to all comments
- Make requested changes promptly
- Thank reviewers for their time

## Communication

### Daily Standup (Async)
Post in Whatsapp/Teams/Slack/Discord:
```
Yesterday: [What you completed]
Today: [What you're working on]
Blockers: [Any issues]
```

### Asking for Help
```
Problem: [Clear description]
What I tried: [Steps taken]
Expected: [What should happen]
Actual: [What actually happens]
Code: [Relevant code snippet or link]
```

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)

## Questions?

- Create an issue with the `question` label
- Ask in team Whatsapp/Slack/Discord channel
- Contact team lead


**Thank you for efforts!**

# Detailed Control Flows (Interaction Logic)

This document visualizes the complex internal logic gates, conditionals, and interaction sequences executing specifically within the `ServiceImpl` architecture layers. Unlike Sequence diagrams mapping controller-to-repository handshakes, these flowcharts track data mutation states.

## 1. Leave Logic Parsing Algorithm (Apply Leave)

When an Employee attempts to submit a Leave, the system calculates duration and overlaps natively before invoking persistence.

```mermaid
flowchart TD
    Start([Receive Application DTO]) --> FetchA(Fetch Existing Employee `LeaveBalance`)
    FetchA --> FetchB(Fetch Confirmed `CompanyHolidays`)
    FetchB --> Duration{Calculate Date Array}
    
    Duration --> |Strip Weekends| Math(Days Requested = End - Start - Weekends)
    Math --> |Strip Holidays| Math2(Days Requested -= Intersecting Holidays)
    Math2 --> Balance{Check Balance}
    
    Balance -- Requested > Remaining --> Err1([Throw LeaveAllocationException<br>'Insufficient Days'])
    Balance -- Requested <= Remaining --> DB{Query Overlaps}
    
    DB --> |Result = Null| Save(Apply PENDING State -> db.save)
    DB --> |Result = True| Err2([Throw LeaveAllocationException<br>'Overlap Exists'])
    
    Save --> Notify(Trigger Manager Notification)
    Notify --> End([Return Success Payload])
```

## 2. Leave Adjudication Logic (Manager Action)

Tracking the data mutations that occur once a manager clicks "Approve".

```mermaid
flowchart TD
    Start([Manager Clicks Approve]) --> Check1(Fetch Leave Application ID)
    Check1 --> CheckAuth{Verify Authority}
    
    CheckAuth -- Unauthorized --> Block([Throw SecurityException])
    CheckAuth -- Authorized --> StateCheck{Check Current Status}
    
    StateCheck -- Status != PENDING --> Invalid([Throw IllegalStateException<br>'Already Processed'])
    StateCheck -- Status == PENDING --> FetchBal(Fetch Employee's Current LeaveBalance)
    
    FetchBal --> Math(Balance = Balance - Leave.RequestedDays)
    
    Math --> Trans[Open Transactional Mutator]
    Trans --> SaveBal(save updated LeaveBalance)
    SaveBal --> SaveL(set Leave to APPROVED; save)
    SaveL --> Close[Commit Transaction]
    
    Close --> Notify(Dispatch Approval Alert to Employee)
    Notify --> End([Return Success])
```

## 3. The Performance Review State Machine

Performance Reviews are finite-state-machines. A record traverses a rigid mapping of statuses which controls accessibility at the Controller layer.

```mermaid
stateDiagram-v2
    [*] --> DRAFT : Manager Initiates Cycle
    
    state DRAFT {
        direction LR
        [*] --> Setup
        Setup --> Validating_Metrics
    }
    
    DRAFT --> PENDING_SELF_ASSESSMENT : Broadcasts to Employee
    
    state PENDING_SELF_ASSESSMENT {
        direction LR
        Validating_Form --> Submit
        Submit --> Map_To_Entity
    }
    
    PENDING_SELF_ASSESSMENT --> PENDING_MANAGER_REVIEW : Employee Locks Forms
    
    state PENDING_MANAGER_REVIEW {
        direction LR
        Load_Employee_Comments --> Fill_Manager_Scores
        Fill_Manager_Scores --> Calculate_Overall_Rating
    }
    
    PENDING_MANAGER_REVIEW --> COMPLETED_REVIEW : Manager Finalizes Rating
    
    COMPLETED_REVIEW --> [*]
```

## 4. Goal Completion Background Automation

When an employee invokes `updateProgress()`, the system asserts the updated numbers to automatically seal records escaping manual oversight.

```mermaid
flowchart TD
    Start([Receive Update Request]) --> Valid{Validate Number Range}
    Valid -- (n < 0 OR n > 100) --> Err([Throw ValidationException])
    Valid -- (0 <= n <= 100) --> Load(Fetch Goal Entity)
    
    Load --> Condition{Evaluate Completion Threshold}
    
    Condition -- (n < 100) --> Save1(Assign n% to Goal)
    Save1 --> Ret1([Return Process])
    
    Condition -- (n == 100) --> Trig(Trigger Automation)
    Trig --> State(Assign GoalStatus.COMPLETED)
    State --> Time(Assign completedDate = now())
## 5. Authentication & Login Control Flow

This sequence models the gating checkpoints protecting the application from brute force and unauthorized entry.

```mermaid
flowchart TD
    Start([User Submits Credentials]) --> Fetch(Search `Employee` by Email/ID)
    Fetch --> Exists{Record Exists?}
    
    Exists -- No --> Err1([Throw BadCredentialsException])
    Exists -- Yes --> Lock{Is Account Locked?}
    
    Lock -- Yes --> CheckTime{Lockout Expired?}
    CheckTime -- No --> Err2([Throw LockedException])
    CheckTime -- Yes --> Unlock(Set Locked = False, Reset Attempts)
    
    Unlock --> Match{Check Password Hash}
    Lock -- No --> Match
    
    Match -- Invalid --> IncCount(Increment Failed Attempts)
    IncCount --> Thresh{Attempts >= 5?}
    Thresh -- Yes --> LockAcc(Set Locked = True, Log Audit)
    Thresh -- No --> SaveInc(Save Employee) --> Err1
    LockAcc --> Err1
    
    Match -- Valid --> FW{Is First Login?}
    FW -- Yes --> RedirectUpdate([Force Redirect to Change Password])
    FW -- No --> ResetCount(Reset Failed Attempts to 0)
    
    ResetCount --> SetLastTime(Update lastLogin Timestamp)
    SetLastTime --> End([Generate JWT / Establish Session])
```

## 6. Password Recovery Control Flow

Flow mapping a user asserting identity via secondary markers (Security Questions) to restore standard access.

```mermaid
flowchart TD
    Start([Click Forgot Password]) --> Req(User inputs Email)
    Req --> Fetch(Search `Employee` By Email)
    Fetch --> Null{Exists?}
    Null -- Null --> End([Return Generic 'Check Email' message to prevent enumeration])
    
    Null -- Exists --> LoadQ(Fetch SecurityQuestions mapped to Employee)
    LoadQ --> Prompt(Prompt User for Answers)
    
    Prompt --> Sub([User Submits Answers])
    Sub --> Match{Answers Match DB Hashes?}
    
    Match -- Invalid --> IncQ(Increment Lockout Tracker)
    Match -- Valid --> GenTok(Generate time-limited ResetToken)
    
    GenTok --> SendTok(Dispatch ResetToken via Email/UI)
    SendTok --> PromptNew(Prompt user for New Password + Token)
    
    PromptNew --> TokVal{Is Token Valid & Unexpired?}
    TokVal -- Invalid --> Err([Throw InvalidTokenException])
    TokVal -- Valid --> HashNew(BCrypt Hash new Password)
    
    HashNew --> Save(db.save & Set FirstLogin=False)
    Save --> DropTok(Invalidate ResetToken)
    DropTok --> EndVal([Account Restored. Redirect to Login])
```

## 7. Audit Logging System Flow

Invisible background flow: Every `save`, `update`, and `delete` within critical `ServiceImpl` modules intercepts here asynchronously.

```mermaid
flowchart LR
    Origin([Service Layer Mutation e.g. 'deleteGoal']) --> Intercept(Execute Aspect @AfterReturning)
    
    Intercept --> Build(Construct `AuditLog` Object)
    Build --> MapA(Map Action E.g., 'DELETE')
    MapA --> MapB(Map Target Table/Entity ID)
    MapB --> MapC(Map SecurityContext Principal ID string)
    MapC --> MapD(Map IP Address/User Agent string from RequestContext)
    
    MapD --> Thread(Spawn Async Background Thread)
    Thread --> Insert([db.save AuditLog. Returns Context])
```

## 8. Employee Onboarding Flow (Admin Action)

Visualizes how the system builds out a new employee tree when the Administrator initializes one.

```mermaid
flowchart TD
    Start([Admin Submits EmployeeDTO]) --> ValidateD(Validate Unique Email/Phone constraints)
    ValidateD --> ValidateDep(Validate Department & Designation Exists)
    
    ValidateDep --> GenerateID(Generate Sequential EmployeeID)
    GenerateID --> GeneratePass(Generate random securely-crypt Temporary Password)
    GeneratePass --> HashPass(BCrypt Hash Temporal Password)
    
    HashPass --> SetupEntity(Construct `Employee` Entity)
    SetupEntity --> SetRole(Attach standard `ROLE_EMPLOYEE`)
    SetRole --> SetLock(Set FirstLogin = TRUE)
    
    SetLock --> SaveTx[Transactional boundary: db.save Employee]
    
    SaveTx --> CreateBalances(Gen 3 default `LeaveBalance` entities for year)
    CreateBalances --> TriggerEmail(Dispatch Temporal Password via Email)
    TriggerEmail --> End([Employee Provisioned])
```

## 9. Manager: Revoke Approved Leave Flow

Tracking exceptional circumstances where an already approved timeline must be forcefully disrupted by Leadership.

```mermaid
flowchart TD
    Start([Manager initiates Revoke on Approved Leave ID]) --> Load(Fetch `LeaveApplication` Entity)
    Load --> Auth{Is Subordinate of Manager?}
    Auth -- No --> Err([SecurityException])
    Auth -- Yes --> State{Status == APPROVED?}
    
    State -- No --> Err2([IllegalStateException: Cannot Revoke Unapproved])
    State -- Yes --> Time{Start Date in Past?}
    
    Time -- Yes --> Err3([Throw Exception: Cannot alter active/past spans])
    Time -- No --> Exec[Begin Transaction]
    
    Exec --> Stat(Update Application Status -> REVOKED)
    Stat --> LoadBal(Fetch Employee `LeaveBalance`)
    LoadBal --> Refund(Refund Days to LeaveBalance Pool)
    
    Refund --> SaveAL(db.save Application)
    SaveAL --> SaveBL(db.save Balance)
    SaveBL --> Commit[End Transaction]
    
    Commit --> Alert(Dispatch Urgency Notification to Employee)
    Alert --> End([Success])
```

## 10. Administrator: Global Profile/Configuration Update Flow

System-level updates (Data synchronization, Holiday manipulation) execute cautiously differently from user-space logic.

```mermaid
flowchart TD
    Start([Admin Modifies Global Profile e.g. Adds Holiday]) --> Validate(Check Date Doesn't Exist)
    Validate --> Exec[Begin Transaction]
    Exec --> Insert(Insert new `Holiday` Entity)
    Insert --> Commit[End Transaction]
    
    Commit --> TriggerAsync(Spawn Asynchronous Recalculator)
    TriggerAsync --> QueryLev(Search all PENDING `LeaveApplications` encompassing date)
    
    QueryLev --> LoopLev{Process List}
    LoopLev --> |Loop Applications| Recalc(Detect if requested duration changes)
    Recalc --> Math(Math: Duration -= 1 if Match Holiday)
    Math --> SaveL(db.save updated PENDING duration)
    SaveL --> Next(Loop)
    
## 11. Profile Update Flow

Mapping how non-critical employee profile information is sanitized and attached, avoiding modifications to restricted properties like roles or locked states.

```mermaid
flowchart TD
    Start([User Submits Profile Update DTO]) --> Map(Extract Updatable Fields: Address, Phone, Contacts)
    Map --> Fetch(Fetch Current `Employee` State by ID)
    
    Fetch --> Attach(Update object properties in-memory)
    Attach --> Validate{Standard Constraint Validations Passed?}
    
    Validate -- No --> Err([Throw ValidationException])
    Validate -- Yes --> Exec[Begin Transaction]
    
    Exec --> Save(db.save Employee)
    Save --> Commit[End Transaction]
    
    Commit --> Alert(Dispatch 'Profile Updated' standard notification)
    Alert --> End([Return Success & Fresh Profile Payload])
```

## 12. Admin: Leave Quota Assignment/Generation Flow

At the beginning of a fiscal reporting year, Admins trigger logic to generate new blank arrays of `LeaveBalance` properties for all active employees.

```mermaid
flowchart TD
    Start([Admin Triggers Annual Quota Refresh]) --> Fetch(Query `Employee` where Status = ACTIVE)
    Fetch --> FetchT(Fetch Active `LeaveType` configurations)
    
    FetchT --> LoopE{Process Employee Batch}
    
    LoopE --> |Next Employee| LoopL{Process Leave Types}
    
    LoopL --> |Next type| Calc(Calculate `totalAllocated` from `LeaveType` defaultDays)
    Calc --> Query{Does Balance Year exist?}
    
    Query -- Yes --> Skip(Skip to avoid overwrite) --> LoopL
    Query -- No --> Gen(Instantiate new LeaveBalance for Year)
    Gen --> SaveT(Add to Transaction Array) --> LoopL
    
    LoopL --> |Batch Finished| Wait(Await next Employee) --> LoopE
    
    LoopE --> |List Depleted| Dump[Execute Bulk db.saveAll array]
    Dump --> Log(Write AuditLog for execution complete)
    Log --> End([All active Employees populated with N+1 metrics])
```

## 13. Goal Performance Assessment Pipeline

Tracking how iterative `progress` updates trigger calculations against the eventual overall evaluation weight map.

```mermaid
flowchart TD
    Start([Manager Requests Employee Evaluation Matrix]) --> Fetch1(Fetch Employee `PerformanceReview`)
    Fetch1 --> Fetch2(Fetch all COMPLETED `Goals` assigned to Employee)
    
    Fetch2 --> Init(Initialize Matrix Processor)
    Init --> Loop{Loop Goals}
    
    Loop --> |Next Goal| CalcP(Calculate DateDifference = deadline - completedAt)
    CalcP --> EvalP{DateDifference > 0?}
    EvalP -- Yes --> WeightA(Assign 1.2x Punctuality Weight)
    EvalP -- No --> WeightB(Assign 0.8x Punctuality Weight)
    
    WeightA --> Store(Store Goal Metric Result) --> Loop
    WeightB --> Store --> Loop
    
    Loop --> |End| Aggr(Aggregate Weights into Recommended FinalScore)
    Aggr --> Map(Map Score into `PerformanceReviewDTO` recommendation variable)
    Map --> End([Render Pre-filled Review Screen for Manager])
```

## 14. Global Notification Broadcasting Flow

Charting how the Admin or System Broadcast components effectively loop and spam asynchronous alert events down the tree without blocking primary HTTP request threads.

```mermaid
flowchart TD
    Start([System Triggers `BroadcastMessage`]) --> Pull(Extract Audience Parameter)
    Pull --> Route{Determine Target Audience}
    
    Route -- ALL --> F1(Query All ACTIVE Employees)
    Route -- MANAGERS --> F2(Query Employees with ROLE_MANAGER)
    Route -- DEPARTMENT_X --> F3(Query Employees mapped to ID)
    
    F1 --> ExecuteAsync[Spawn Asynchronous ForkJoin Pool]
    F2 --> ExecuteAsync
    F3 --> ExecuteAsync
    
    ExecuteAsync --> Loop{Process ID array in parallel}
    
    Loop --> |Thread Execution| Create(Instantiate `Notification`)
    Create --> Inject(Inject Content & Target ID)
    Inject --> Save(db.save Entity)
    Save --> Next(Loop)
    
    Loop --> |Array Depleted| End([Background Thread Terminates])
```

## 15. Reports Extraction (Export to Presentation Layer)

Tracking the huge computational funnel gathering system states for mapping to DataVisualization components (e.g. pie charts).

```mermaid
flowchart TD
    Start([Request `GET /api/reports/utilization`]) --> P1(Spawn parallel Data Fetchers)
    
    P1 --> F1(Query: count all Leaves grouped by Month)
    P1 --> F2(Query: count all Performance Ratings grouped by Value)
    P1 --> F3(Query: count active Goals grouped by Status)
    
    F1 --> Sync[Wait for Thread Barrier]
    F2 --> Sync
    F3 --> Sync
    
    Sync --> Merge(Merge resulting tuple sets into centralized Data Map)
    Merge --> Build(Construct `ReportDTO`)
    
    Build --> Sanitize(Strip any sensitive entity relational mappings)
    Sanitize --> JSON(Serialize Object to standard JSON structure)
    JSON --> End([Return 200 HTTP Response])
```

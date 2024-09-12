# Project directory path
cd awseventbridgeexample/ || { echo "Project directory not found"; exit 1; }

# Path to the event data JSON file
EVENT_DATA_FILE="../eventData1.json"
JAVA_FILE="src/main/java/com/example/aws/eventbridge/example/awseventbridgeexample/AwseventbridgeexampleApplication.java"


# Check if the JSON file exists
if [ ! -f "$EVENT_DATA_FILE" ]; then
    echo "Event data file not found: $EVENT_DATA_FILE"
    exit 1
fi


# Loop through each object in the JSON array
jq -c '.[]' "$EVENT_DATA_FILE" | while read -r event_data; do

    # Escape special characters for insertion into Java multi-line string
    escaped_event_data=$(echo "$event_data" | sed 's/"/\\"/g')

    # Insert the escaped event data into the Java file's 'detail' string
    sed -i.bak "/String detail = \"\"\"/a\\
                $escaped_event_data
                " "$JAVA_FILE"

    echo ""
    echo "Inserted event data into the Java file: "
    echo ""
    echo "$event_data"
    echo ""
    echo "Starting Spring Boot application..."
    echo ""

    # Run the Spring Boot application
    ./mvnw spring-boot:run

    # Wait for a short period to avoid overwhelming resources
    sleep 2

    # Revert the Java file to its original state after each run
    mv "$JAVA_FILE.bak" "$JAVA_FILE"
    sleep 10

done

# Wait for all background processes to finish
wait

echo "All Spring Boot applications have been started."

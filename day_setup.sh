set -eu

usage() {
    echo "Usage: ./day_setup <day>"
    echo "  day: The day of the month to setup. Must be a number."
}

day=$1

if ! [[ $day =~ ^[0-9]+$ ]]; then
  usage
  exit 1
fi

template_file="src/main/kotlin/days/DayTemplate.kt"
new_dir="src/main/kotlin/days/day$day"
new_file="$new_dir/Day$day.kt"

mkdir "$new_dir"
cp $template_file $new_file
# Replace all instances of day01 with day$day, and Day01 with Day$day
sed -i '' "s|package days|package days.day$day|g" "$new_file"
sed -i '' "s|day01|day$day|g" "$new_file"
sed -i '' "s|Day01|Day$day|g" "$new_file"

# Sara Wille
       .data
nums:   .word 5, 8, 12, 14, 16, 19, 23, 28, 35, 39, 41, 43, 44, 52, 55, 58, 66, 72, 74, 76, 81
length: .word 21
str:    .asciiz "Searching from index "
to:     .asciiz " to "
endl:   .asciiz "\n"

        .text
        #
        # This code loads arguments into $a registers and calls the search routine.
        # Once we get back from sorting it prints the returned value and exits.
        #
main:
        li $a0, 0       # left index value
        la $t0, length  # size is right index value
        lw $a1, 0($t0)  
        la $a2, nums    # pass array's base address in $a2
        li $a3, 19     # value to search for in array
        # Args are all loaded into $a register -- time to jump to search procedure
        jal binary_search       
        # Now we're back, with the return value in $v0
        move $a0, $v0   # Move result into $a0 to print
        li $v0, 1
        syscall         # Print the result
        li $v0, 10      # syscall 10 is exit
        syscall         
    
        
# PROCEDURE:  print_status
#  Prints a line of output that describes current search region.  
#  NOTE: This routine alters the $a register values.
#
# Inputs:
#  $a0  Low index of search region
#  $a1  High index of search region
# 
# Outputs:
#  None
        
print_status:
        addi $sp, $sp, -8   # Make room for two words on stack
        sw   $a0, 0($sp)    # Store initial $a0 value on stack
        sw   $a1, 4($sp)    # Store initial $a1 value on stack
        # Get on with the printing
        la $a0, str     # put address of str in $a0 for syscall
        li $v0, 4       # print string syscall #
        syscall         # print the bulk of the string
        lw $a0, 0($sp)  # bring $a0 (low) in from stack
        li $v0, 1       # print integer syscall #
        syscall
        la $a0, to      # put address of " to " string in $a0
        li $v0, 4       # print string syscall #
        syscall
        lw $a0, 4($sp)  # bring in $a1 (high) from stack, put in $a0
        li $v0, 1       # print integer syscall #
        syscall
        la $a0, endl    # put address of newline string in $a0
        li $v0, 4
        syscall
        addi $sp, $sp, 8
        jr $ra
        

# PROCEDURE:  binary_search
#  Searches for a specific value in an array using binary search.
#
# Inputs:
#  $a0  Index where search begins (inclusive)(min)
#  $a1  Index where search ends (exclusive)(max)
#  $a2  Base address of array
#  $a3  Value to search for
# 
# Outputs:
#  $v0  Contains the position within the array at which value occurs, or
#       where it *would* be located if it's not in the array currently.
binary_search:

	addi $sp, $sp, -12	# make room for saved values THAT CHANGE ($a0, $a1, $ra)
				# store registers
	sw $a0, 0($sp)
	sw $a1, 4($sp) 
	sw $ra, 8($sp)
	jal print_status	# print status
				# need to get our saved values back though (print_status alters $a0, $a1, $ra)
	lw $a0, 0($sp)
	lw $a1, 4($sp) 
	lw $ra, 8($sp)
	addi $t0, $a0, 1	# $t0 = low + 1
	bne  $t0, $a1, split 	#if low + 1 not equal to high, go to split
	j end			#otherwise, we found our index and we go to end
split:
	#figure our which side to recurse on
	add $t0, $a1, $a0	
	div $s0, $t0, 2		# calculate mid ((high + low)/2) and put in $s0 
	sll $t1, $s0, 2		# mult offset * 4
	add $t1, $t1, $a2	# add byte offset to base address
	lw $t1, 0($t1)		# load nums[mid] into $t1
	slt $t2, $a3, $t1 	# $t2 0 if recurse right, 1 if recurse left
	beq $t2, $zero, right	# recurse appropriately
	j left
right:
	#recurse right
	move $a0, $s0		# low now mid
	j binary_search	# do it again
left:
	#recurse left 
	move $a1, $s0		# high now mid
	j binary_search	#do it again
end:
	move $v0, $a0		#put our low position in return register
	lw $ra, 8($sp)		#get our return address
	addi $sp, $sp, 12	#pop the stack
	jr $ra			#go back to return address
        
